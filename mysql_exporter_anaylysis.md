# prometheus > mysql_exporter
 * https://github.com/prometheus/mysqld_exporter


 ## go 언어 설치 (1.13 version이 설치됨)
```
$ sudo apt install golang-go
```

 ## sample go 실행
```
package main

import "fmt"

func main() {
    name := "Go Developers"
    fmt.Println("Azure for", name)
}
```
 실행 사진
<br>
<img src="https://github.com/RainingCodes/mysql_performance_schema/blob/main/img/img22.JPG" width="500px" height="150px" alt="sample 실행 화면"></img><br/>

 ## custom exporter
  * 참고 사이트 : https://gywn.net/2021/07/make-own-query-exporter-with-go/

 ### 1. 빈 exporter

참고사이트 코드 활용
```
package main

import (
	"flag"
	"net/http"

	"github.com/prometheus/client_golang/prometheus"
	"github.com/prometheus/client_golang/prometheus/promhttp"
	"github.com/prometheus/common/version"
	log "github.com/sirupsen/logrus"
)

func main() {
	// =====================
	// Get OS parameter
	// =====================
	var bind string
	flag.StringVar(&bind, "bind", "0.0.0.0:9104", "bind")
	flag.Parse()

	// ========================
	// Regist handler
	// ========================
	prometheus.Register(version.NewCollector("query_exporter"))

	// Regist http handler
	http.HandleFunc("/metrics", func(w http.ResponseWriter, r *http.Request) {
		h := promhttp.HandlerFor(prometheus.Gatherers{
			prometheus.DefaultGatherer,
		}, promhttp.HandlerOpts{})
		h.ServeHTTP(w, r)
	})

	// start server
	log.Infof("Starting http server - %s", bind)
	if err := http.ListenAndServe(bind, nil); err != nil {
		log.Errorf("Failed to start http server: %s", err)
	}
}

```

go.mod 파일은 직접 작성
```
module main
 
go 1.13          
```

실행시키기 위해 필요한 package 설치

```
$ go mod vendor
```
<br>
<img src="https://github.com/RainingCodes/mysql_performance_schema/blob/main/img/img23.JPG" width="650px" height="800px" alt="vendor"></img><br/>

실행

```
$ go run main.go
```

실행 화면
<br>
<img src="https://github.com/RainingCodes/mysql_performance_schema/blob/main/img/img25.JPG" width="900px" height="300px" alt="실행"></img><br/>
다른 터미널로 수집된 정보 확인
<br>
<img src="https://github.com/RainingCodes/mysql_performance_schema/blob/main/img/img26.JPG" width="800px" height="500px" alt="실행2"></img><br/>


 ### 2. 호스트별 커넥션 수, 유저별 커넥션 쿼리 작성
 참고사이트 코드 활용해서 코드 수정 (main.go를 아래 링크에 있는 코드 처럼 변경)
```
package main
 
import (
    "flag"
    "io/ioutil"
    "net/http"
    "os"
 
    "github.com/ghodss/yaml"
    "github.com/prometheus/client_golang/prometheus"
    "github.com/prometheus/client_golang/prometheus/promhttp"
    "github.com/prometheus/common/version"
    log "github.com/sirupsen/logrus"
)
 
var config Config
 
func main() {
    var err error
    var configFile, bind string
    // =====================
    // Get OS parameter
    // =====================
    flag.StringVar(&configFile, "config", "config.yml", "configuration file")
    flag.StringVar(&bind, "bind", "0.0.0.0:9104", "bind")
    flag.Parse()
 
    // =====================
    // Load config & yaml
    // =====================
    var b []byte
    if b, err = ioutil.ReadFile(configFile); err != nil {
        log.Errorf("Failed to read config file: %s", err)
        os.Exit(1)
    }
 
    // Load yaml
    if err := yaml.Unmarshal(b, &config); err != nil {
        log.Errorf("Failed to load config: %s", err)
        os.Exit(1)
    }
 
    // ========================
    // Regist handler
    // ========================
    log.Infof("Regist version collector - %s", "query_exporter")
    prometheus.Register(version.NewCollector("query_exporter"))
 
    // Regist http handler
    log.Infof("HTTP handler path - %s", "/metrics")
    http.HandleFunc("/metrics", func(w http.ResponseWriter, r *http.Request) {
        h := promhttp.HandlerFor(prometheus.Gatherers{
            prometheus.DefaultGatherer,
        }, promhttp.HandlerOpts{})
        h.ServeHTTP(w, r)
    })
 
    // start server
    log.Infof("Starting http server - %s", bind)
    if err := http.ListenAndServe(bind, nil); err != nil {
        log.Errorf("Failed to start http server: %s", err)
    }
}
 
// =============================
// Config config structure
// =============================
type Config struct {
    DSN     string
    Metrics map[string]struct {
        Query       string
        Type        string
        Description string
        Labels      []string
        Value       string
        metricDesc  *prometheus.Desc
    }
}
```
실행시키기 위해 필요한 package 설치

```
$ go mod vendor
```

config.yml 파일 생성 후, mysql 접속 정보 입력
```
dsn: user1:user1@tcp(localhost:3306)/information_schema
metrics:
  process_count_by_host:
    query: "select user, substring_index(host, ':', 1) host, count(*) sessions from information_schema.processlist group by 1,2 "
    type: gauge
    description: "process count by host"
    labels: ["user","host"]
    value: sessions
  process_count_by_user:
    query: "select user, count(*) sessions from information_schema.processlist group by 1 "
    type: gauge
    description: "process count by user"
    labels: ["user"]
    value: sessions

```

터미널을 새로 하나 띄워 promethus에 정보 등록
```
$ cd ~/prometheus-2.8.0.linux-amd64
$ vi prometheus.yml
```
prometheus.yml 파일에 아래 내용 추가
```
- job_name: 'query_exporter'
    scrape_interval : 5s
    static_configs:
    - targets: ['0.0.0.0:9104']
```

실행 (터미널 두 개 이용 하나는 go 실행, 하나는 prometheus 실행)

```
$ go run main.go
```

<br>
<img src="https://github.com/RainingCodes/mysql_performance_schema/blob/main/img/img27.JPG" width="800px" height="300px" alt="실행3"></img><br/>

~/prometheus-2.8.0.linux-amd64 위치에 있는 터미널
```
$ ./prometheus
```
<br>
<img src="https://github.com/RainingCodes/mysql_performance_schema/blob/main/img/img28.JPG" width="900px" height="600px" alt="실행4"></img><br/>

### 3. Collector 작성

main.go 아래처럼 수정
```
package main
 
import (
    "database/sql"
    "flag"
    "io/ioutil"
    "net/http"
    "os"
    "strconv"
    "strings"
 
    "github.com/ghodss/yaml"
    _ "github.com/go-sql-driver/mysql"
    "github.com/prometheus/client_golang/prometheus"
    "github.com/prometheus/client_golang/prometheus/promhttp"
    "github.com/prometheus/common/version"
    log "github.com/sirupsen/logrus"
)
 
var config Config
 
const (
    collector = "query_exporter"
)
 
func main() {
    var err error
    var configFile, bind string
    // =====================
    // Get OS parameter
    // =====================
    flag.StringVar(&configFile, "config", "config.yml", "configuration file")
    flag.StringVar(&bind, "bind", "0.0.0.0:9104", "bind")
    flag.Parse()
 
    // =====================
    // Load config & yaml
    // =====================
    var b []byte
    if b, err = ioutil.ReadFile(configFile); err != nil {
        log.Errorf("Failed to read config file: %s", err)
        os.Exit(1)
    }
 
    // Load yaml
    if err := yaml.Unmarshal(b, &config); err != nil {
        log.Errorf("Failed to load config: %s", err)
        os.Exit(1)
    }
 
    // ========================
    // Regist handler
    // ========================
    log.Infof("Regist version collector - %s", collector)
    prometheus.Register(version.NewCollector(collector))
    prometheus.Register(&QueryCollector{})
 
    // Regist http handler
    log.Infof("HTTP handler path - %s", "/metrics")
    http.HandleFunc("/metrics", func(w http.ResponseWriter, r *http.Request) {
        h := promhttp.HandlerFor(prometheus.Gatherers{
            prometheus.DefaultGatherer,
        }, promhttp.HandlerOpts{})
        h.ServeHTTP(w, r)
    })
 
    // start server
    log.Infof("Starting http server - %s", bind)
    if err := http.ListenAndServe(bind, nil); err != nil {
        log.Errorf("Failed to start http server: %s", err)
    }
}
 
// =============================
// Config config structure
// =============================
type Config struct {
    DSN     string
    Metrics map[string]struct {
        Query       string
        Type        string
        Description string
        Labels      []string
        Value       string
        metricDesc  *prometheus.Desc
    }
}
 
// =============================
// QueryCollector exporter
// =============================
type QueryCollector struct{}
 
// Describe prometheus describe
func (e *QueryCollector) Describe(ch chan<- *prometheus.Desc) {
    for metricName, metric := range config.Metrics {
        metric.metricDesc = prometheus.NewDesc(
            prometheus.BuildFQName(collector, "", metricName),
            metric.Description,
            metric.Labels, nil,
        )
        config.Metrics[metricName] = metric
        log.Infof("metric description for \"%s\" registerd", metricName)
    }
}
 
// Collect prometheus collect
func (e *QueryCollector) Collect(ch chan<- prometheus.Metric) {
 
    // Connect to database
    db, err := sql.Open("mysql", config.DSN)
    if err != nil {
        log.Errorf("Connect to database failed: %s", err)
        return
    }
    defer db.Close()
 
    // Execute each queries in metrics
    for name, metric := range config.Metrics {
 
        // Execute query
        rows, err := db.Query(metric.Query)
        if err != nil {
            log.Errorf("Failed to execute query: %s", err)
            continue
        }
 
        // Get column info
        cols, err := rows.Columns()
        if err != nil {
            log.Errorf("Failed to get column meta: %s", err)
            continue
        }
 
        des := make([]interface{}, len(cols))
        res := make([][]byte, len(cols))
        for i := range cols {
            des[i] = &res[i]
        }
 
        // fetch database
        for rows.Next() {
            rows.Scan(des...)
            data := make(map[string]string)
            for i, bytes := range res {
                data[cols[i]] = string(bytes)
            }
 
            // Metric labels
            labelVals := []string{}
            for _, label := range metric.Labels {
                labelVals = append(labelVals, data[label])
            }
 
            // Metric value
            val, _ := strconv.ParseFloat(data[metric.Value], 64)
 
            // Add metric
            switch strings.ToLower(metric.Type) {
            case "counter":
                ch <- prometheus.MustNewConstMetric(metric.metricDesc, prometheus.CounterValue, val, labelVals...)
            case "gauge":
                ch <- prometheus.MustNewConstMetric(metric.metricDesc, prometheus.GaugeValue, val, labelVals...)
            default:
                log.Errorf("Fail to add metric for %s: %s is not valid type", name, metric.Type)
                continue
            }
        }
    }
}
```

module 필요한 거 추가 install
```
go mod vendor
```
실행 (터미널 두 개 이용 하나는 go 실행, 하나는 prometheus 실행)

```
$ go run main.go
```
```
$ ./prometheus
```
실행 화면 (localhost:9090 접속 > status > targets 확인하면 볼 수 있음)
<br>
<img src="https://github.com/RainingCodes/mysql_performance_schema/blob/main/img/img29.JPG" width="1000px" height="300px" alt="실행5"></img><br/>


 ### 4. mutex instances 갯수 세는 쿼리 포함하도록 변경
 config.yml의 내용에 아래와 같은 쿼리 추가
 ```
 count_mutex_instances:
    query: "select count(*) mutex_instances from performance_schema.mutex_instances "
    type: gauge
    description: "count of mutex instances"
    labels: ["mutex_instances"]
    value: mutex_instances
```
실행 (터미널 두 개 이용 하나는 go 실행, 하나는 prometheus 실행)

```
$ go run main.go
```
```
$ ./prometheus
```
실행 화면 (localhost:9090 접속 > status > targets 확인하면 볼 수 있음)
<br>
<img src="https://github.com/RainingCodes/mysql_performance_schema/blob/main/img/img30.JPG" width="900px" height="1000px" alt="실행6"></img><br/>


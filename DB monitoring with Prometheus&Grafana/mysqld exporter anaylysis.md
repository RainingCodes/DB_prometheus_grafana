# prometheus > mysql_exporter
 * https://github.com/prometheus/mysqld_exporter


## 코드 받아오기

```
$ mkdir mysqld_exporter_src
$ cd ~/mysqld_exporter_src
$ git clone https://github.com/prometheus/mysqld_exporter.git
```
git clone 시 mysqld_exporter 폴더 생김

## go 버전 변경(기존에 설치한 버전이 아닌 최신 버전으로 설정하기)
* 참고 사이트 : https://developer-kus.tistory.com/44
* 참고 사이트2 : https://askubuntu.com/questions/720260/updating-golang-on-ubuntu

기존 go 삭제
```
$ sudo apt-get purge golang*
```
go 최신 버전 다운로드 및 압축 해제
```
$ wget https://golang.org/dl/go1.18.3.linux-amd64.tar.gz
$ tar -xvf go1.18.3.linux-amd64.tar.gz 
```
환경변수 설정
```
$sudo vi $HOME/.profile
```
파일 아래에 아래 내용 추가
```
# GOROOT: 설치된 Go 패키지의 경로
export GOROOT=$HOME/go

# GOPATH: Go 패키지를 이용해 작업할 공간
export GOPATH=$HOME/

# 동일
export PATH=$GOPATH:$GOROOT/bin:$PATH
```

실행 방법 ~/mysqld_exporter_src/mysqld_exporter 위치에서
```
$ go build .
```
실행 사진
<br>
<img src="https://github.com/RainingCodes/mysql_performance_schema/blob/main/img/img31.JPG" width="900px" height="1000px" alt="실행1"></img><br/>

업데이트가 필요하다는 안내에 따라 
```
$ go mod tidy
```
실행할 땐 아래 명령 수행
```
$ go run .
```

## collector 만들기 (mutex_instances 갯수를 보여주는 query문 실행)
~/mysqld_exporter_src/mysqld_exporter/collector에서 다른 go file들을 보면서
비슷하게 작성함

```
$ vi perf_schema_mutex_instances.go
```
파일 내용
```
package collector

import (
        "context"
        "database/sql"

        "github.com/go-kit/log"
        "github.com/prometheus/client_golang/prometheus"
)

const perfMutexInstancesQuery = `
        SELECT
            count(*) mutex_instances
          FROM performance_schema.mutex_instances
        `


// Metric descriptors.
var (
        performanceSchemaMutexInstancesDesc = prometheus.NewDesc(
                prometheus.BuildFQName(namespace, performanceSchema, "mutex_instances"),
                "the number of mutex_instances",
                []string{"mutex_instances"}, nil,
        )
)

// ScrapePerfFileInstances collects from `performance_schema.file_summary_by_instance`.
type ScrapePerfMutexInstances struct{}

// Name of the Scraper. Should be unique.
func (ScrapePerfMutexInstances) Name() string {
        return "perf_schema.mutex_instances"
}

// Help describes the role of the Scraper.
func (ScrapePerfMutexInstances) Help() string {
        return "Collect metrics from performance_schema.mutex_instances"
}

// Version of MySQL from which scraper is available.
func (ScrapePerfMutexInstances) Version() float64 {
        return 5.5
}

// Scrape collects data from database connection and sends it over channel as prometheus metric.
func (ScrapePerfMutexInstances) Scrape(ctx context.Context, db *sql.DB, ch chan<- prometheus.Metric, logger log.Logger) error {
        // Timers here are returned in picoseconds.
        perfSchemaMutexInstancesRows, err := db.QueryContext(ctx, perfMutexInstancesQuery)
        if err != nil {
                return err
        }
        defer perfSchemaMutexInstancesRows.Close()

        var (
                name           string
                count          uint64
        )
        name = "Mutex Instances"

        for perfSchemaMutexInstancesRows.Next() {
                if err := perfSchemaMutexInstancesRows.Scan(
                        &count,
                ); err != nil {
                        return err
                }
                ch <- prometheus.MustNewConstMetric(
                        performanceSchemaMutexInstancesDesc, prometheus.CounterValue, float64(count),
                        name,
                )
        }
        return nil
}

// check interface
var _ Scraper = ScrapePerfMutexInstances{}
```

main 함수가 있는 go file은 ~/mysqld_exporter_src/mysqld_exporter/ 위치에 존재함
<br>
main 함수에서 scraper 등록하는 부분에 수정이 필요함
```
vi mysqld_exporter.go
```
맨 아래에 새로 만든 scrape 등록하는 한 줄을 넣음
```
(윗부분 생략)
// scrapers lists all possible collection methods and if they should be enabled by default.
var scrapers = map[collector.Scraper]bool{
        collector.ScrapeGlobalStatus{}:                        true,
        collector.ScrapeGlobalVariables{}:                     true,
        collector.ScrapeSlaveStatus{}:                         true,
        collector.ScrapeProcesslist{}:                         false,
        collector.ScrapeUser{}:                                false,
        collector.ScrapeTableSchema{}:                         false,
        collector.ScrapeInfoSchemaInnodbTablespaces{}:         false,
        collector.ScrapeInnodbMetrics{}:                       false,
        collector.ScrapeAutoIncrementColumns{}:                false,
        collector.ScrapeBinlogSize{}:                          false,
        collector.ScrapePerfTableIOWaits{}:                    false,
        collector.ScrapePerfIndexIOWaits{}:                    false,
        collector.ScrapePerfTableLockWaits{}:                  false,
        collector.ScrapePerfEventsStatements{}:                false,
        collector.ScrapePerfEventsStatementsSum{}:             false,
        collector.ScrapePerfEventsWaits{}:                     false,
        collector.ScrapePerfFileEvents{}:                      false,
        collector.ScrapePerfFileInstances{}:                   false,
        collector.ScrapePerfMemoryEvents{}:                    false,
        collector.ScrapePerfReplicationGroupMembers{}:         false,
        collector.ScrapePerfReplicationGroupMemberStats{}:     false,
        collector.ScrapePerfReplicationApplierStatsByWorker{}: false,
        collector.ScrapeUserStat{}:                            false,
        collector.ScrapeClientStat{}:                          false,
        collector.ScrapeTableStat{}:                           false,
        collector.ScrapeSchemaStat{}:                          false,
        collector.ScrapeInnodbCmp{}:                           true,
        collector.ScrapeInnodbCmpMem{}:                        true,
        collector.ScrapeQueryResponseTime{}:                   true,
        collector.ScrapeEngineTokudbStatus{}:                  false,
        collector.ScrapeEngineInnodbStatus{}:                  false,
        collector.ScrapeHeartbeat{}:                           false,
        collector.ScrapeSlaveHosts{}:                          false,
        collector.ScrapeReplicaHost{}:                         false,
        collector.ScrapePerfMutexInstances{}:                  true,
}
(아랫 부분 생략)
```

실행 방법 ~/mysqld_exporter_src/mysqld_exporter 위치에서
```
$ go build .
$ ./mysqld_exporter
```

prometheus를 실행시켜 변수가 잘 등록되었는지 확인
<br>
<img src="https://github.com/RainingCodes/mysql_performance_schema/blob/main/img/img32.JPG" width="900px" height="1000px" alt="실행2"></img><br/>
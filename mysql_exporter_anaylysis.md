# prometheus > mysql_exporter
 * https://github.com/prometheus/mysqld_exporter


 ## go 언어 설치
 * https://go.dev/
 * window version으로 설치함

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
<img src="https://github.com/RainingCodes/mysql_performance_schema/blob/main/img/img22.JPG" width="2500px" height="300px" alt="sample 실행 화면"></img><br/>

 ## custom exporter
  * 참고 사이트 : https://gywn.net/2021/07/make-own-query-exporter-with-go/

 ### 1. 빈 exporter

<a></a>

실행시키기 위해 필요한 package 설치
```
go get github.com/prometheus/client_golang/prometheus
go get github.com/prometheus/client_golang/prometheus/promauto
go get github.com/prometheus/client_golang/prometheus/promhttp
go get github.com/prometheus/common/version
go get github.com/sirupsen/logrus
```
실행 화면
<br>
<img src="https://github.com/RainingCodes/mysql_performance_schema/blob/main/img/img23.JPG" width="2500px" height="300px" alt="빈 exporter 실행 화면"></img><br/>

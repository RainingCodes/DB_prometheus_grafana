# postgresql custom query 추가하기

## 오류 없이 실행하기
* postgres exporter 실행 후 나타나는 error
 <br>
<img src="https://github.com/RainingCodes/mysql_performance_schema/blob/main/img/img67.JPG" width="900px" height="300px" alt="postgres exporter"></img><br/>

    - https://github.com/prometheus-community/postgres_exporter 의 설명을 주로 참고
    - postgres_exporter.yml 파일이 없어서 error 발생
    - dsn 설정이 이뤄지지 않아 Collector가 제대로 만들어지지 않는 error 발생

* postgres_exporter.yml 작성
  - https://github.com/prometheus-community/postgres_exporter#auth_modules
  - postgres_exporter 와 같은 위치에 postgres_exporter.yml 작성
  ```
  $ vi postgres_exporter.yml
  auth_modules:
        node1:
                userpass:
                        username: postgres
                        password: postgres
                options:
                        sslmode: disable
  ```
* dsn 설정하기
  - https://github.com/prometheus-community/postgres_exporter#setting-the-postgres-servers-data-source-name
  - data source를 지정하고 실행시키기 (실행 위치 postgres_exporter가 있는 위치) 
  ```
  $ sudo -u postgres DATA_SOURCE_NAME="user=postgres host=/var/run/postgresql/ sslmode=disable" ./postgres_exporter
  ```
* custom query 추가하기
  - 디렉터리 내에 queries.yaml 파일을 보면 측정하는 parameter를 추가하는 예제 쿼리가 있음
  - https://github.com/prometheus-community/postgres_exporter#adding-new-metrics-via-a-config-file
  - flag 리스트를 살펴보면 extend.query.path를 통해 queries.yaml의 parameter를 추가 후 postgres_expoerter를 실행시킬 수 있음
  - https://github.com/prometheus-community/postgres_exporter#adding-new-metrics-via-a-config-file
  ```
  $ sudo -u postgres DATA_SOURCE_NAME="user=postgres host=/var/run/postgresql/ sslmode=disable" ./postgres_exporter --extend.query-path queries.yaml
  ```

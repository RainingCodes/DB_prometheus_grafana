# 설치 해야하는 목록

# 1. mysql
* 참고 사이트 : https://velog.io/@seungsang00/Ubuntu-%EC%9A%B0%EB%B6%84%ED%88%AC%EC%97%90-MySQL-%EC%84%A4%EC%B9%98%ED%95%98%EA%B8%B0
  
  ## 설치 과정
    ### ubuntu update
    ```
    $ sudo apt-get update
    ```
  ## mysql server 설치 (비밀번호 입력은 전부 생략)
    ```
    $ sudo apt-get install mysql-server
    ```
  ## 외부 접속 기능 허용
    ```
    $ sudo ufw allow mysql
    ```
  ## mysql 접속 (수동)
    ```
    $ sudo systemctl start mysql
    ```
  ## mysql 자동 접속 설정
    ```
    $ sudo systemctl enable mysql
    ```
  ## mysql 실행 
    ### 1. root 계정으로 접속(root 계정 비밀번호 입력 필요)
      $ sudo mysql -u root -p
      
    실행 화면  
    <img src="https://github.com/RainingCodes/mysql_performance_schema/blob/main/img/img3.JPG?raw=true" width="700px" height="400px" alt="mysql 실행"></img>
    
    ### 2. TESTDB 생성
      mysql> CREATE DATABASE TESTDB;
      mysql> SHOW DATABASES;
    실행 화면
    <br>
    <img src="https://github.com/RainingCodes/mysql_performance_schema/blob/main/img/img4.JPG?raw=true" width="200px" height="150px" alt="db상태"></img>

    ### 3. user1 계정 생성 (id를 user1으로 정함)
      mysql> CREATE USER 'user1'@'localhost' IDENTIFIED BY '{본인이 지정하고 싶은 비밀번호}';
      mysql> FLUSH PRIVILEGES;
      mysql> SELECT User, Host, authentication_string FROM mysql.user; 
  
    ### 4. user1 계정 권한 부여 (id를 user1으로 정함)
      mysql> GRANT ALL PRIVILEGES ON TESTDB.* FOR 'user1'@'localhost';
      mysql> FLUSH PRIVILEGES;
      mysql> SHOW GRANTS FOR user1@localhost;
    실행화면
    <br>
    <img src="https://github.com/RainingCodes/mysql_performance_schema/blob/main/img/img5.JPG?raw=true" width="4000px" height="300px" alt="권한확인"></img>

    ### 5. user1 계정 접속(비밀번호 입력 없이 접속됨)
    ```
    $ mysql -u user1 -p
    ```
    
    ### 6. mysql version 확인
      $ mysql --version
    실행화면
    <br>
    <img src="https://github.com/RainingCodes/mysql_performance_schema/blob/main/img/img6.JPG?raw=true" width="700px" height="100px" alt="버전확인"></img>


# 2. prometheus
* 참고 사이트 : https://blog.naver.com/PostView.naver?blogId=bokmail83&logNo=221487998797&parentCategoryNo=&categoryNo=39&viewDate=&isShowPopularPosts=true&from=search
* https://prometheus.io/download/ 에서 prometheus-2.8.0.linux-am64.tar.gz 다운받음
  ## 설정 과정
    ### prometheus 설정 파일 prometheus.yml 확인
    ```
    $ cd prometheus-2.8.0.linux-amd64
    $ vi prometheus.yml
    ```
    ### prometheus.yml

      # my global config
      global:
        scrape_interval:     15s # Set the scrape interval to every 15 seconds. Default is every 1 minute.
        evaluation_interval: 15s # Evaluate rules every 15 seconds. The default is every 1 minute.
        # scrape_timeout is set to the global default (10s).
      
      # Alertmanager configuration
      alerting:
        alertmanagers:
        - static_configs:
          - targets:
            # - alertmanager:9093
      
      # Load rules once and periodically evaluate them according to the global 'evaluation_interval'.
      rule_files:
        # - "first_rules.yml"
        # - "second_rules.yml"
      
      # A scrape configuration containing exactly one endpoint to scrape:
      # Here it's Prometheus itself.
      scrape_configs:
        # The job name is added as a label `job=<job_name>` to any timeseries scraped from this config.
        - job_name: 'prometheus'

          # metrics_path defaults to '/metrics'
          # scheme defaults to 'http'.

          static_configs:
          - targets: ['localhost:9090']

    
    ### prometheus 실행 (실행 위치 ~/prometheus-2.8.0.linux-amd64)
      $ ./prometheus
      
    웹브라우저로 localhost:9090 접속
    <br>
    <img src="https://github.com/RainingCodes/mysql_performance_schema/blob/main/img/img7.JPG?raw=true" width="700px" height="400px" alt="prometheus 접속"></img>

  

# 3. node_exporter
* 2번과 비슷하게 설정함, 참고 사이트 https://wooseok-uzi.tistory.com/3
* 서버 시스템/상태를 확인할 수 있음
* https://prometheus.io/download/#node_exporter 에서 node_exporter-1.3.1.linux-amd64.tar.gz 다운받음
* 기본 포트 : 9100

  ## prometheus 서버에 설정하기
    ### prometheus.yml 파일의 scrape_configs에 아래 내용 추가
      - job_name : 'node_exporter'
        scrape_interval : 5s
        static_configs:
        - targets: ['localhost:9100']
        - 
  ## 실행방법
    ```
    $ cd node_exporter-1.3.1.linux-amd64/
    $ ./node_exporter
    ```
  prometheus로 실행 확인
  localhost:9090에 접속 후 Status>Targets
    <br>
    <img src="https://github.com/RainingCodes/mysql_performance_schema/blob/main/img/img8.JPG?raw=true" width="4000px" height="100px" alt="node_exporter 확인"></img>
  
  node 관련 실행 후 graph 확인
  <br>
    <img src="https://github.com/RainingCodes/mysql_performance_schema/blob/main/img/img9.JPG?raw=true" width="700px" height="400px" alt="node_exporter graph 예시"></img>


## 4. mysql exporter
* 2번과 동일한 사이트 참고
* https://prometheus.io/download/#mysqld_exporter 에서mysqld_exporter-0.14.0.linux-amd64.tar.gz 다운받음
* 기본 포트 : 9104

  ## .my.cnf 설정 (mysql 계정 연동)
  1번에서 만든 mysql의 계정을 연결하는 설정파일 .my.cnf를 만들어준다. <br>
  내 conf 파일 위치 : /home/user1/.my.cnf (~/.my.cnf)
  ```
  [client]
  user=user1
  password={1번에서 지정한 비밀번호를 넣으세요}
  ```
  
  ## prometheus 서버에 설정하기
    ### prometheus.yml 파일의 scrape_configs에 아래 내용 추가
      - job_name: 'mysql'
        scrape_interval : 5s
        static_configs:
        - targets: ['localhost:9104']

  ## 실행방법
    ```
    $ cd mysqld_exporter-0.14.0.linux-amd64/
    $ ./mysqld_exporter
    ```
  prometheus로 실행 확인
  localhost:9090에 접속 후 Status>Targets
    <br>
    <img src="https://github.com/RainingCodes/mysql_performance_schema/blob/main/img/img10.JPG?raw=true" width="4000px" height="300px" alt="mysqld_exporter 확인"></img>
  
### prometheus에서 node_exporter & mysql exporter 연동
* 참고 사이트 : http://mysqldbadmtech.blogspot.com/2016/11/pmm-060-pmm-client-mysqld-exporter.html
* <img src="https://github.com/RainingCodes/mysql_performance_schema/blob/main/img/img1.JPG" width="500px" height="400px" alt="연동 이미지"></img><br/>

# 4. grafana
* 참고 사이트 : https://blog.naver.com/PostView.naver?blogId=bokmail83&logNo=221489127312&parentCategoryNo=&categoryNo=39&viewDate=&isShowPopularPosts=true&from=search
* 다운로드 사이트 : https://grafana.com/grafana/download 
* graphana-enterprise_8.5.4_linux-amd64.tar.gz 다운받아 압축 품 
  ```
  wget https://dl.grafana.com/enterprise/release/grafana-enterprise-8.5.4.linux-amd64.tar.gz
  tar -zxvf grafana-enterprise-8.5.4.linux-amd64.tar.gz
  ```
  다운로드 후 압축 풀린 게 담겨있는 디렉토리 ~/grafana-8.5.4


  ## 실행방법  
    ### 1. grafana server 실행

      $ cd ~/grafana-8.5.4/bin
      $ ./grafana-server

    
    ### 2. localhost:3000 접속

    <img src="https://github.com/RainingCodes/mysql_performance_schema/blob/main/img/img11.JPG?raw=true" width="700px" height="600px" alt="prometheus 접속"></img>
      <br>
        최초 접속시 
      <br>id : admin
      <br>pw : admin
      <br> --> pw 변경하도록 안내
  
    
    ### 3. Data Source 추가하기
    Configuration > Data Sources 클릭
    <br>
     <img src="https://github.com/RainingCodes/mysql_performance_schema/blob/main/img/img12.JPG" width="500px" height="100px" alt="grafana 로그인 후 화면"></img><br/>
    Add data source 클릭
    <br>
     <img src="https://github.com/RainingCodes/mysql_performance_schema/blob/main/img/img13.JPG" width="900px" height="500px" alt="grafana add data source"></img><br/>

    prometheus 추가
    <br>
    <img src="https://github.com/RainingCodes/mysql_performance_schema/blob/main/img/img2.JPG" width="500px" height="800px" alt="grafana에 prometheus 추가"></img><br/>

    ### 3. DashBoard 추가하기
    * 참고 사이트 : https://programming.vip/docs/prometheus-grafana-visual-monitoring-mysql.html
    
    평점이 제일 높은 dashboard 하나를 골라 선택
    https://grafana.com/grafana/dashboards/7362

    Create > import 클릭
    <br>
     <img src="https://github.com/RainingCodes/mysql_performance_schema/blob/main/img/img14.JPG" width="500px" height="300px" alt="grafana import1"></img><br/>
    id 7369 입력 후 Load 버튼 클릭
    <br>
    <img src="https://github.com/RainingCodes/mysql_performance_schema/blob/main/img/img15.JPG" width="500px" height="500px" alt="grafana import2"></img><br/>

    아래처럼 설정
    <br>
    <img src="https://github.com/RainingCodes/mysql_performance_schema/blob/main/img/img16.JPG" width="500px" height="500px" alt="grafana impor3"></img><br/>




## mysql 실행
  ### mysql user1으로 접속
  ```
  $ mysql -u user1 -p TESTDB
  ```
  ### TESTDB 사용 및 간단한 table 만들기 실행
  ```
  mysql> use TESTDB
  mysql> 
  ```

  connection error 시
  https://dongle94.github.io/ubuntu/ubuntu-check-ip/
  참고해서 설정할 것
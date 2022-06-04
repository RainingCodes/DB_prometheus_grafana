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
      mysql> CREATE USER 'user1'@'localhost' IDENTIFIED BY '{본인 지정 비번}';
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
* 2번 참고 사이트 + https://wooseok-uzi.tistory.com/3
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
    <img src="https://github.com/RainingCodes/mysql_performance_schema/blob/main/img/img8.JPG?raw=true" width="700px" height="400px" alt="node_exporter 확인"></img>
  
  node 관련 실행 후 graph 확인
  <br>
    <img src="https://github.com/RainingCodes/mysql_performance_schema/blob/main/img/img9.JPG?raw=true" width="700px" height="400px" alt="node_exporter graph 예시"></img>


## 3. mysql exporter
* 참고 사이트 : 3번의 두번째 사이트
* mysqld_exporter-0.14.0.linux-amd64 다운받음
* 내 conf 파일 위치 : /home/user1/.my.cnf
* 기본 포트 : 9104
  
### prometheus에서 node_exporter & mysql exporter 연동
* 참고 사이트 : http://mysqldbadmtech.blogspot.com/2016/11/pmm-060-pmm-client-mysqld-exporter.html
* <img src="https://github.com/RainingCodes/mysql_performance_schema/blob/main/img/img1.JPG" width="500px" height="400px" alt="연동 이미지"></img><br/>

## 4. grafana
* 다운로드 사이트 : https://grafana.com/grafana/download
* graphana-enterprise_8.5.3_linux-amd64 다운받음
* graphana 실행 관련 참고 사이트 : https://ssup2.github.io/record/Grafana_%EC%84%A4%EC%B9%98_%EC%8B%A4%ED%96%89_Ubuntu_18.04/
* admin / admin으로 설정되어 있는 id pw에서 pw 변경함
*  <img src="https://github.com/RainingCodes/mysql_performance_schema/blob/main/img/img2.JPG" width="500px" height="750px" alt="연동 이미지"></img><br/>


### mysql user1으로 TESTDB 접속
* mysql -u user1 -p TESTDB
# jdbc로 java 프로그램 mysql 연동하기
참고자료  : https://ppost.tistory.com/entry/MySQL%EA%B3%BC-JAVA-%EC%97%B0%EB%8F%99%EC%9D%84-%EC%9C%84%ED%95%9C-MySQL-Connector-%EB%8B%A4%EC%9A%B4-%EB%B0%8F-%EC%84%A4%EC%B9%98


## 1. jdbc dwonload
https://www.mysql.com/products/connector/ 사이트에 접속해 jdbc download를 누르고 os를 linux로 선택<br>
mysql-connector-java_8.0.29-1ubuntu21.10_all.deb를 다운받음 <br>
패키지 설지
```
$ sudo dpkg -i mysql-connector-java_8.0.29-1ubuntu21.10_all.deb 
```
jdbc jar file $JAVA_HOME/lib로 이동 & $JAVA_HOME/jre/lib로 이동
```
$ sudo cp /usr/share/java/mysql-connector-java-8.0.29.jar $JAVA_HOME/lib
$ sudo cp /usr/share/java/mysql-connector-java-8.0.29.jar $JAVA_HOME/jre/lib
```


## 2. 환경변수 설정
```
$ sudo vi /etc/profile
```
파일 열어서 맨 아래에 아래 내용 추가
```
export JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64
PATH=$JAVA_HOME/bin:$PATH
export CLASSPATH=.:$JAVA_HOME/lib/mysql-connector-java.8.0.29.jar
```
변경내용 적용
```
$ source /etc/profile
```

## 3. 예제 jdbc
```
import java.sql.*;
 class Lab1 {
    public static void main(String argv[]) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("jdbc 드라이버 로딩 성공");
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }

        try {
            String url = "jdbc:mysql://localhost:3306/{DB명}";
            Connection con =
            DriverManager.getConnection(url,"{id}","{password}");
            System.out.println("mysql 접속 성공");
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select s.stu_id from student s, takes t where s.stu_id = t.stu_id and t.grade >= 'B';");
            //예제 Query임, 자신의 DB 상황에 맞게 query문 수정
            System.out.println("Got result:");

            while(rs.next()) {
                String id= rs.getString(1);
                System.out.println(" id = " + id);
            }

            stmt.close();
            con.close();
        } catch(java.lang.Exception ex) {
            ex.printStackTrace();
        }
    }
}
```

실행결과
<br>
<img src="https://github.com/RainingCodes/mysql_performance_schema/blob/main/img/img33.JPG" width="500px" height="500px" alt="java"></img><br/>

<br>
<img src="https://github.com/RainingCodes/mysql_performance_schema/blob/main/img/img34.JPG" width="500px" height="500px" alt="mysql"></img><br/>
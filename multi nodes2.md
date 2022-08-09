# 목표 환경
<br>
<img src="https://github.com/RainingCodes/mysql_performance_schema/blob/main/img/img46.jpg" width="1000px" height="500px" alt="goal"></img><br/>

 * Node1에서는 Node2, Node3 둘 다 monitoring 할 수 있는 화면에 접근이 가능 
   - (<a href="https://github.com/RainingCodes/mysql_performance_schema/blob/main/multi%20nodes.md">multi nodes</a>)에서 구성
 * Node2에서는 자신만 monitoring 할 수 있는 화면에 접근 가능
 * Node3에서는 자신만 monitoring 할 수 있는 화면에 접근 가능

## 각 Node monitoring 화면 구성
 * Mysql Overview Dashboard 추가 (Node3를 추가하는 것으로 작성)
   - import button click
  <br>
<img src="https://github.com/RainingCodes/mysql_performance_schema/blob/main/img/img47.JPG" width="800px" height="600px" alt="new dashboard"></img><br/>
   - mysql overview dashboard(ID : 7362)를 load
  <br>
<img src="https://github.com/RainingCodes/mysql_performance_schema/blob/main/img/img48.JPG" width="800px" height="600px" alt="new dashboard 2"></img><br/>
   - name 설정 & Unique Identifier(UID) 설정
  <br>
<img src="https://github.com/RainingCodes/mysql_performance_schema/blob/main/img/img49.JPG" width="800px" height="600px" alt="new dashboard 3"></img><br/>

 * dashboard setting 변경 (Node3만 보이게)
  - dashboard가 만들어진 후 우측 상단에 톱니바퀴 모양(Setting) click
  <br>
<img src="https://github.com/RainingCodes/mysql_performance_schema/blob/main/img/img50.JPG" width="800px" height="600px" alt="dashboard setting1"></img><br/>
  - Variables Tab click
  - Host Click
  <br>
<img src="https://github.com/RainingCodes/mysql_performance_schema/blob/main/img/img51.JPG" width="800px" height="600px" alt="dashboard setting2"></img><br/>
  - Regex를 이용한 filter (Node3만 monitoring 하도록)
  - 참고 사이트 : https://grafana.com/docs/grafana/latest/variables/filter-variables-with-regex/
  <br>
<img src="https://github.com/RainingCodes/mysql_performance_schema/blob/main/img/img52.JPG" width="800px" height="600px" alt="dashboard setting3"></img><br/>
 * Dashboard 별 Permission 설정 
  (https://grafana.com/docs/grafana/next/administration/user-management/manage-dashboard-permissions/) --> User 별로 권한 설정할 수 있음
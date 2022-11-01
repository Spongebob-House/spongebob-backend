<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<%@ include file="/WEB-INF/views/common/logincheck.jsp" %>
<script
            type="text/javascript"
            src="//dapi.kakao.com/v2/maps/sdk.js?appkey=b97c0d315c9af1251f9514bf8ffe4279&libraries=services"
></script>

    <main>
      <section class="container-fluid bg-black justify-content-center text-center">
          <form class="search-box" method="GET" action="${root }/map/search" >
            <div class="row col-md-12 mb-2 justify-content-center text-center">
              <div class="row form-group col-lg-2 col-md-2 ps-3 pe-3 justify-content-center align-items-center">
                <select class="form-select bg-white text-black " id="sido" name="sido">
                  <option value="">시도선택</option>
                </select>
              </div>
              <div class="row form-group col-lg-2 col-md-2 ps-3 pe-3 justify-content-center align-items-center">
                <select class="form-select bg-white text-black" id="gugun" name="gugun">
                  <option value="">구군선택</option>
                </select>
              </div>
              <div class="row form-group col-lg-2 col-md-2 ps-3 pe-3 justify-content-center align-items-center">
                <select class="form-select bg-white text-black" id="dong" name="dong">
                  <option value="">동선택</option>
                </select>
              </div>
              <div class="row form-group col-lg-1 col-md-1 ps-3 pe-3 justify-content-center align-items-center">
                <select class="form-select bg-white text-black" id="year" name="year"></select>
              </div>
              <div class="row form-group col-lg-1 col-md-1 ps-3 pe-3 justify-content-center align-items-center">
                <select class="form-select bg-white text-black" id="month" name="month">
                  <option value="">매매월선택</option>
                </select>
              </div>
              <div class="row form-group col-lg-2 col-md-2 ps-3 pe-3 justify-content-center align-items-center">
                <button type="button" id="list-btn" class="btn btn-outline-light" style="transition:0.2s;">
                  매매 정보 가져오기
                </button>
              </div>
               <div class="row form-group col-lg-2 col-md-2 ps-3 pe-3 justify-content-center align-items-center">
                <button type="button" id="inter-add-btn" class="btn btn-outline-primary" style="transition:0.2s;">
                  관심 지역 등록
                </button>
              </div>
           </div>
          </form>
      </section>
      <section class="inter-box p-2" >
      <div class="font-weight-bold">관심 지역</div>
      <div id= "interlist" style="	overflow-x:auto; white-space: nowrap;" >
      <c:forEach var="inter" items="${interList }">
      	 <button class="btn-inter btn btn-warning" data-no="${inter.dongCode }" type="button">${inter.dongName } <a href="${root }/map/delinter?dong=${inter.dongCode}">X</a></button>
      </c:forEach>      
      </div>
      <form id="inter-form" method="POST" action="${root }/map/addinter">
      	<input type="hidden" id="ndong" name="dong" value=""/>
      </form>
      </section>
      <section class="home-result-box">
        <div class="search-result">
          <div class="table-box col-sm-12 col-md-3 overflow-auto">
            <table class="table table-hover text-center col-md-4 col-sm-12">
              <thead>
              <tr>
                <th>거래내역</th>
              </tr>
              </thead>
              <tbody id="aptlist">
              <c:if test="${empty mapList}">
              <tr>
              <td>거래내역 없음</td>
              </tr>
              </c:if>
              <c:if test="${!empty mapList}">
              <c:forEach var="apt" items="${mapList}" varStatus="st">
              <tr class="apt-item" no="${st.index}"lat = "${apt.lat }" lng="${apt.lng }">
              	<td>
              	<div class="apt-name">
              		<a>${apt.apartmentName }</a>
              	</div>
				<div>
				<c:if test="${!empty apt.coffee }">
				    <span style="font-size:12px" class="text-success"><image src="${root }/assets/img/coffee.png" width="20" height="20" class="icon"></image> ${apt.coffee.name} ${apt.coffee.dist}m</span>
				</c:if>
				<c:if test="${!empty apt.metro }">
              		<span style="font-size:12px"><image src="${root }/assets/img/metro.png" width="20" height="20" class="icon"></image> ${apt.metro.name} ${apt.metro.dist}m</span>
				</c:if>
				
				</div>
              	<div class="apt-space">
              		면적 : ${apt.area }
              	</div>
              	<div class="apt-price">
              		거래금액 : ${apt.dealAmount }만원
              	</div>
              	<div class="apt-date text-end">
              		<span>
						<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" class="icon" viewBox="0 0 24 24"><path d="M17 12h-5v5h5v-5zM16 1v2H8V1H6v2H5c-1.11 0-1.99.9-1.99 2L3 19c0 1.1.89 2 2 2h14c1.1 0 2-.9 2-2V5c0-1.1-.9-2-2-2h-1V1h-2zm3 18H5V8h14v11z"></path></svg>              		
              		</span>
              		${apt.dealYear}.${apt.dealMonth}.${apt.dealDay }
              	</div>
              	</td>
              </tr>
              </c:forEach>
              </c:if>              
            </tbody>
            </table>
          </div>
          <div id="home-map" class="col-sm-12 col-md-9" style="min-height: 700px"></div>
  <script>
  let aptPoint = [];
  let bounds = new kakao.maps.LatLngBounds();;
  let infos = [];
  let markers = [];
  var container = document.getElementById("home-map");
	const imageSrc = "${root}/assets/img/home.png"; // 마커이미지의 주소입니다
	let imageSize = new kakao.maps.Size(40, 44); // 마커이미지의 크기입니다
	let imageOption = { offset: new kakao.maps.Point(27, 69) }; // 마커이미지의 옵션입니다. 마커의 좌표와 일치시킬 이미지 안에서의 좌표를 설정합니다.
  var options = {
		  center: new kakao.maps.LatLng(37.5012767241426, 127.039600248343), 
		  level: 8,
			};
  var map = new kakao.maps.Map(container, options);
	// 마커의 이미지정보를 가지고 있는 마커이미지를 생성합니다
	var markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize, imageOption);

  

 </script>
        	<c:if test="${!empty mapList}">
        	<c:forEach var="apt" items="${mapList}">
        	<script>
            var coords = new kakao.maps.LatLng("${apt.lat}"*1,"${apt.lng}"*1);
            //var message = String(apt.querySelector("거래금액").textContent) + "만원"; // 인포윈도우에 표시될 내용입니다
            aptPoint.push(new kakao.maps.LatLng("${apt.lat}"*1,"${apt.lng}"*1));
            bounds.extend(new kakao.maps.LatLng("${apt.lat}"*1,"${apt.lng}"*1));
            map.setBounds(bounds);
            var marker = new kakao.maps.Marker({
              map: map,
              position: coords,
              image:markerImage,
            });
    		var iwContent = `<div style="padding:5px; font-size:14px;"><div>${apt.apartmentName}</div><div>주소 : ${apt.dong} ${apt.jibun}</div><div>건축년도 : ${apt.buildYear}년</div></div>`, // 인포윈도우에 표출될 내용으로 HTML 문자열이나 document element가 가능합니다
       		iwRemoveable = true; // removeable 속성을 ture 로 설정하면 인포윈도우를 닫을 수 있는 x버튼이 표시됩니다

       		// 인포윈도우를 생성합니다
       		var infowindow = new kakao.maps.InfoWindow({
       		    content : iwContent,
       		    removable : iwRemoveable
       		});

        	infos.push(infowindow);
       		// 마커 위에 인포윈도우를 표시합니다
            markers.push(marker);  
	          </script>
			</c:forEach>
			</c:if>

          
        </div>
      </section>
    </main>
    <script>
    //search form 제출 관련.
   	document.querySelector("#list-btn").addEventListener("click",function(){
   		let sido = document.querySelector("#sido").value;
   		let gugun = document.querySelector("#gugun").value;
   		let dong = document.querySelector("#dong").value;
   		let year = document.querySelector("#year").value;
   		let month = document.querySelector("#month").value;
   		if(sido==""){
   			alert("시도를 선택해주세요.");
   		}
   		else if(gugun==""){
   			alert("구군을 선택해주세요.");
   		}
   		else if(dong==""){
   			alert("동을 선택해주세요.");
   		}
   		else if(year=="" && month==""){
   			document.querySelector(".search-box").submit();
   		}
   		else if(month==""){
   			alert("월을 선택해주세요.");
   		}
   		else if(year == ""){
   			alert("년도를 선택해주세요.");
   		}
   		else{
   			document.querySelector(".search-box").submit();
   		}
   		
   	});

    //관심지역 등록
   	document.querySelector("#inter-add-btn").addEventListener("click",function(){
   		let sido = document.querySelector("#sido").value;
   		let gugun = document.querySelector("#gugun").value;
   		let dong = document.querySelector("#dong").value;

   		if(sido==""){
   			alert("시도를 선택해주세요.");
   		}
   		else if(gugun==""){
   			alert("구군을 선택해주세요.");
   		}
   		else if(dong==""){
   			alert("동을 선택해주세요.");
   		}
   		else{
   			let form = document.querySelector("#inter-form");
   			form.querySelector("#ndong").value= dong;
   			form.submit();
   		}
   	});
	
    let inters = document.querySelectorAll(".btn-inter");
    if(inters!=null){	
    	inters.forEach(function(inter){
    		inter.addEventListener("click",function(){
   				let form = document.querySelector("#inter-form");
   				form.setAttribute("action", "${root }/map/search");
   				form.querySelector("#ndong").value= inter.getAttribute("data-no");
   				form.submit();
    		});
    	});
    }


    let apts = document.querySelectorAll(".apt-item");
    let no = -1;
  	apts.forEach(function(apt){
		apt.addEventListener("click",function(){
			if(no!=-1){
				infos[no].setMap(null);
			}
			no = apt.getAttribute("no");
			console.log(no);
			console.log(infos[no], markers[no]);
	   		infos[no].open(map, markers[no]); 		
	   		map.setCenter(new kakao.maps.LatLng(parseFloat(apt.getAttribute("lat")), parseFloat(apt.getAttribute("lng"))));
	      	map.setLevel(1);
			
		});
    	
  	});
  	

  	
    </script>  
    
    <script type="text/javascript" src="${root }/assets/js/main.js"></script>      
<%@ include file="/WEB-INF/views/common/footer.jsp" %>
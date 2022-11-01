<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<%@ include file="/WEB-INF/views/common/logincheck.jsp" %>
<c:set var = 'sel' value="selected"></c:set>
<c:if test="${articles == null}">
	<script type="text/javascript">
		alert("정상적인 URL 접근이 아닙니다.");
		location.href = "${root}/board/list?pgno=1&key=&word=";
	</script>
</c:if>
      <div class="row justify-content-center">
        <div class="col-lg-8 col-md-10 col-sm-12">
          <h2 class="my-3 py-3 shadow-sm bg-light text-center">
            <mark class="sky">공지사항</mark>
          </h2>
        </div>
        <div class="col-lg-8 col-md-10 col-sm-12">
          <div class="row align-self-center mb-2">
            <div class="col-md-2 text-start">
          <c:if test="${userinfo.userId == 'admin'}">
              <button type="button" id="btn-mv-register" class="btn btn-outline-primary btn-sm">
                글쓰기
              </button>
              <script>
	              document.querySelector("#btn-mv-register").addEventListener("click", function () {
	                location.href = "${root}/board/write";
	              });              
             </script>
          </c:if>
            </div>
            <div class="col-md-7 offset-3">
              <form class="d-flex" id="form-search" action="">
              	<input type="hidden" name="act" value="list">
                <select
                  class="form-select form-select-sm ms-5 me-1 w-50"
                  name="key"
                  aria-label="검색조건"
                >
                  <option value=""<c:if test="${empty key}">selected</c:if>>검색조건</option>
                  <option value="subject" <c:if test="${'subject' eq key}">selected</c:if>>제목</option>
                  <option value="userid" <c:if test="${'userid' eq key}">selected</c:if>>작성자</option>
                </select>
                <div class="input-group input-group-sm">
                  <input type="text" class="form-control" name="word" placeholder="검색어..." value="${word}" />
                  <button id="btn-search" class="btn btn-dark" type="button">검색</button>
                </div>
              </form>
            </div>
          </div>
          <table class="table table-hover">
            <thead>
              <tr class="text-center">
                <th scope="col">글번호</th>
                <th scope="col">제목</th>
                <th scope="col">작성자</th>
                <th scope="col">조회수</th>
                <th scope="col">작성일</th>
              </tr>
            </thead>
            <tbody>
            <c:if test="${empty articles }">
            	<tr>
            	<td class = "text-center" colspan="5">
            	검색결과가 존재하지 않습니다.
            	</td>
            	<tr>
            </c:if>
			<c:forEach var="article" items="${articles }">
              <tr class="text-center">
                <th scope="row">${article.articleNo }</th>
                <td class="text-start">
                  <a
                    href="#"
                    class="article-title link-dark"
                    data-no="${article.articleNo }"
                    style="text-decoration: none"
                  >
                    	${article.subject }
                  </a>
                </td>
                <td>${article.userId }</td>
                <td>${article.hit }</td>
                <td>${article.registerTime }</td>
              </tr>			
			</c:forEach>

            </tbody>
          </table>
        </div>
        <div class="row">
          <ul class="pagination justify-content-center">
            <li class="page-item">
              <a class="page-link" id="page-bef" href="#">이전</a>
            </li>
          	<c:forEach var="page" begin="${pageStart }" end="${pageEnd }">
          	<c:if test="${pgno eq page }">
            	<li class="page-item"><a class="page-link page-num active" href="#">${page }</a></li>          	
          	</c:if>
          	<c:if test="${pgno ne page }">
            	<li class="page-item"><a class="page-link page-num" href="#">${page }</a></li>          	
          	</c:if>
          	</c:forEach>

            <li class="page-item"><a class="page-link" id="page-aft" href="#">다음</a></li>
          </ul>
        </div>
      </div>
    </div>
    <form id="form-no-param" method="get" action="">
      <input type="hidden" id="act" name="act" value="">
      <input type="hidden" id="pgno" name="pgno" value="${pgno }">
      <input type="hidden" id="key" name="key" value="${key }">
      <input type="hidden" id="word" name="word" value="${word }">
      <input type="hidden" id="articleno" name="articleno" value="">
    </form>
    <script>
      let titles = document.querySelectorAll(".article-title");
      titles.forEach(function (title) {
        title.addEventListener("click", function () {
        	let form = document.querySelector("#form-no-param");
        	form.setAttribute("action","${root}/board/view");
//         	form.querySelector("#pgno").value="view";
//         	form.querySelector("#key").value="view";
//         	form.querySelector("#word").value="view";
        	form.querySelector("#articleno").value=this.getAttribute("data-no");
        	form.submit();
        });
      });

      
      document.querySelector("#btn-search").addEventListener("click", function () {
    	  let form = document.querySelector("#form-search");
          form.setAttribute("action", "${root}/board/list");
          form.submit();
      });

      
      let pglinks = document.querySelectorAll(".page-num");
      pglinks.forEach(function (pglink) {
    	  pglink.addEventListener("click", function () {
          	let form = document.querySelector("#form-no-param");
          	form.setAttribute("action","${root}/board/list");
           	form.querySelector("#pgno").value= this.innerText;
          	form.submit();
          });
        });
      
      document.querySelector("#page-bef").addEventListener("click",function(){
        	let form = document.querySelector("#form-no-param");
          	form.setAttribute("action","${root}/board/list");
           	form.querySelector("#pgno").value= ${pageStart-pageSize};
          	form.submit();
    	  
      })     
      document.querySelector("#page-aft").addEventListener("click",function(){
        	let form = document.querySelector("#form-no-param");
          	form.setAttribute("action","${root}/board/list");
          	<c:if test="${maxPage ge (pageStart+pageSize)}">
           	form.querySelector("#pgno").value= ${pageStart+pageSize};          	
          	</c:if>
          	<c:if test="${maxPage lt (pageStart+pageSize)}">
          	form.querySelector("#pgno").value= ${pgno};          	
          	</c:if>
          	form.submit();
    	  
      })     
      
    </script>
<%@ include file="/WEB-INF/views/common/footer.jsp" %>

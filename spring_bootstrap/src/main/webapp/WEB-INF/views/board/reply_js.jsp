<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<script src="https://cdnjs.cloudflare.com/ajax/libs/handlebars.js/4.7.7/handlebars.min.js"></script>
<script type="text/x-handlebars-template"  id="reply-list-template" >
{{#each .}}
<div class="replyLi" >
	<div class="user-block">
		<img src="<%=request.getContextPath()%>/member/getPicture.do?id={{replyer}}" class="img-circle img-bordered-sm"/>
    </div>	
	<div class="timeline-item" >
  		<span class="time">
    		<i class="fa fa-clock"></i>{{prettifyDate regdate}}
	 		<a class="btn btn-primary btn-xs {{rno}}-a" id="modifyReplyBtn" 
				data-rno={{rno}} data-replyer={{replyer}}
				onclick="replyModifyModal_go('{{rno}}');"
				style="display:{{visibleByLoginCheck replyer}};"
	    		data-toggle="modal" data-target="#modifyModal">Modify</a>
  		</span>
	
  		<h3 class="timeline-header"><strong style="display:none;">{{rno}}</strong>{{replyer}}</h3>
  		<div class="timeline-body" id="{{rno}}-replytext">{{replytext}}</div>
	</div>
</div>

{{/each}}	
</script>
<script type="text/x-handlebars-template"  id="reply-pagination-template" >
<li class="paginate_button page-item">
	<a href="{{goPage 1}}" aria-controls="example2" data-dt-idx="1" tabindex="0" class="page-link">
		<i class='fas fa-angle-double-left'></i>
	</a>
</li>
<li class="paginate_button page-item">
	<a href="{{#if prev}}{{goPage prevPageNum}}{{else}}{{goPage cri.page}}{{/if}}" aria-controls="example2" data-dt-idx="1" tabindex="0" class="page-link">
		<i class='fas fa-angle-left'></i>
	</a>
</li>
{{#each pageNum}}
<li class="paginate_button page-item {{signActive this}} ">
	<a href="{{goPage this}}" aria-controls="example2" data-dt-idx="1" tabindex="0" class="page-link">{{this}}</a>
</li>
{{/each}}

<li class="paginate_button page-item ">
	<a href="{{#if next}}{{goPage nextPageNum}}{{else}}{{goPage cri.page}}{{/if}}" aria-controls="example2" data-dt-idx="1" tabindex="0" class="page-link">
		<i class='fas fa-angle-right'></i>
	</a>
</li>
<li class="paginate_button page-item">
	<a href="{{goPage realEndPage}}" aria-controls="example2" data-dt-idx="1" tabindex="0" class="page-link">
		<i class='fas fa-angle-double-right'></i>
	</a>
</li>	
</script>




<script>
var replyPage=1;
window.onload=function(){
	getPage("<%=request.getContextPath()%>/reply/list.do?bno=${board.bno}&page="+replyPage);
}

function getPage(pageInfo,page){
	if(page) replyPage = page;
	$.getJSON(pageInfo,function(data){	
		printData(data.replyList,$('#repliesDiv'),$('#reply-list-template'));	
		printPagination(data.pageMaker,$('ul#pagination'),$('#reply-pagination-template'));
	});
}

function printData(replyArr,target,templateObject){
	var template=Handlebars.compile(templateObject.html());
	var html = template(replyArr);	
	$('.replyLi').remove();
	target.after(html);
}

/* pagination */
function printPagination(pageMaker,target,templateObject){
	var pageNumArray = new Array(pageMaker.endPage-pageMaker.startPage+1);
	for(var i=pageMaker.startPage;i<pageMaker.endPage+1;i++){
		pageNumArray[i-pageMaker.startPage]=i;
	}	
	
	pageMaker.pageNum=pageNumArray;  
	pageMaker.prevPageNum=pageMaker.startPage-1;
	pageMaker.nextPageNum=pageMaker.endPage+1;
	
	var template=Handlebars.compile(templateObject.html());	
	var html = template(pageMaker);	
	target.html("").html(html);
}

Handlebars.registerHelper({
	"prettifyDate":function(timeValue){ //Handlbars??? ?????????????????? ??????
						var dateObj=new Date(timeValue);
						var year=dateObj.getFullYear();
						var month=dateObj.getMonth()+1;
						var date=dateObj.getDate();
						return year+"/"+month+"/"+date;
					},
	"visibleByLoginCheck":function(replyer){
		var result="none";		
		if(replyer == "${loginUser.id}") result="visible";		
		return result;						  
	},
	"signActive":function(pageNum){
		if(pageNum == replyPage) return 'active';
	},
	"goPage":function(pageNum){
		return 'javascript:getPage("<%=request.getContextPath()%>/reply/list.do?bno=${board.bno}&page='+pageNum+'",'+pageNum+');';
	}
});
</script>

<script>
function replyRegist_go(){
	var replytext=$('#newReplyText').val();	
	
	var data={
			"bno":"${board.bno}",
			"replyer":"${loginUser.id}",
			"replytext":replytext	
	}
	
	$.ajax({
		url:"<%=request.getContextPath()%>/reply/regist.do",
		type:"post",
		data:JSON.stringify(data),		
		contentType:'application/json',
		success:function(data){
			alert('????????? ?????????????????????.\n????????????????????? ???????????????.');
			replyPage=data; //???????????????
			getPage("<%=request.getContextPath()%>/reply/list.do?bno="+${board.bno}+"&page="+data); //????????? ??????
			$('#newReplyText').val("");	
		},
		error:function(error){
			AjaxErrorSecurityRedirectHandler(error.status);	
		}
	});
}


//?????? ?????? modal
function replyModifyModal_go(rno){
	
	$('div#modifyModal div.modal-body input#replytext').val($('div#'+rno+'-replytext').text());
	$('div#modifyModal div.modal-header h4.modal-title').text(rno);
}

//?????? ??????.
function replyModify_go(){
	//alert("modify btn");
	
	var rno=$('div#modifyModal h4.modal-title').text();
	var replytext=$('div#modifyModal #replytext').val();
	
	var sendData={
			"rno":rno,
			"replytext":replytext
	}
	
	//console.log(sendData);
	
	$.ajax({
		url:"<%=request.getContextPath()%>/reply/modify.do",
		type:"post",
		data:JSON.stringify(sendData),
		contentType:"application/json",
		success:function(result){
			alert("?????????????????????.");			
			getPage("<%=request.getContextPath()%>/reply/list.do?bno=${board.bno}&page="
					+replyPage);
		},
		error:function(error){
			AjaxErrorSecurityRedirectHandler(error.status);	
		},
		complete:function(){
			$('#modifyModal').modal('hide');
		}
	});
}

function replyRemove_go(){
	//alert("delete btn");
	var rno=$('h4.modal-title').text();
	
	//alert(rno);
	$.ajax({
		url:"<%=request.getContextPath()%>/reply/remove.do?rno="+rno+"&page="
				+replyPage+"&bno=${board.bno}",
		type:"get",
		success:function(page){
			alert("?????????????????????.");
			getPage("<%=request.getContextPath()%>/reply/list.do?bno=${board.bno}&page="+page);
			replyPage=page;
		},
		error:function(error){
			AjaxErrorSecurityRedirectHandler(error.status);	
		},
		complete:function(){
			$('#modifyModal').modal('hide');
		}
	});
}
</script>











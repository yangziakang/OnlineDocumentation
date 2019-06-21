var coding_debug = true; // debug

var bmsAjaxOptions = {
	success : function() {
	}, // 提交后的回调函数
	url : "", // 默认是form的action， 如果申明，则会覆盖
	type : "post", // 默认是form的method（get or post），如果申明，则会覆盖
	dataType : "json", // html(默认), xml, script, json...接受服务端返回的类型
	clearForm : false, // 成功提交后，清除所有表单元素的值
	resetForm : false, // 成功提交后，重置所有表单元素的值
	timeout : 30000, // 限制请求的时间，当请求大于30秒后，跳出请求
	contentType : "application/x-www-form-urlencoded; charset=UTF-8",
	global : false,
	async : true,
	data : {},
	complete : function(XMLHttpRequest, textStatus) {
		/* layer.closeAll('loading'); */
		if (XMLHttpRequest.status == 200) {
			var executeStatus = XMLHttpRequest.getResponseHeader("executeStatus"); // 通过XMLHttpRequest取得响应头，sessionstatus，
			var url = XMLHttpRequest.getResponseHeader("url");//
			// var isLogin = XMLHttpRequest.getResponseHeader("isLogin");//
			if (executeStatus == "sessionTimeout") {
				// 如果超时就处理 ，指定要跳转的页面
				_alert("页面过期，请重新登录", function() {
					window.top.location.href = url;
				});
			} else if (executeStatus == "unallowed") {
				_alert("权限不足无法访问");
			} else if (executeStatus == "isRepeatSubmit") {
				_alert("请勿重复提交");
			} else if (executeStatus == "noResourceFound") {
				_alert("您访问的资源不存在");
			} else {
				if (XMLHttpRequest.responseText.indexOf("<") == 0) {
					var objE = document.createElement("div");
					objE.innerHTML = XMLHttpRequest.responseText;
					var code = $(objE).find("#bms_global_errorCode").val();
					if (code) {
						showErrorMsg(XMLHttpRequest.responseText);
					}
				}
			}
		} else if (XMLHttpRequest.statusText == 'timeout' || XMLHttpRequest.statusText == 'Gateway Time-out' || XMLHttpRequest.status == '504') {
			_alert("连接超时，请稍后再试");
		} else if (XMLHttpRequest.statusText == '404') {
			_alert("您访问的资源不存在");
		} else if (!XMLHttpRequest.responseText) {
			_alert("系统异常");
		} 
	}
};

$(function() {
	// ajax前置条件
	$.ajaxSetup(bmsAjaxOptions);
});


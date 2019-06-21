var _code_success = "1";
var _code_failed = "0";

var bmsAjaxOptions = {
		success : function() {
		}, // 提交后的回调函数
		url : "", // 默认是form的action， 如果申明，则会覆盖
		type : "post", // 默认是form的method（get or post），如果申明，则会覆盖
		dataType : "json", // html(默认), xml, script, json...接受服务端返回的类型
		clearForm : false, // 成功提交后，清除所有表单元素的值
		resetForm : false, // 成功提交后，重置所有表单元素的值
		timeout : 30000, // 限制请求的时间，当请求大于30秒后，跳出请求
		contentType : "application/x-www-form-urlencoded;charset=UTF-8",
		global : false,
		async : true,
		data : {}
	};

$(function(){
	$.ajaxSetup(bmsAjaxOptions);
});

var _type_i18n_cn_ZH = {
	"array" : "数组[字符串]",
	"string" : "字符串",
	"array[object]" : "数组[对象]",
	"array[string]" : "数组[字符串]",
	"array[boolean]" : "数组[布尔类型]",
	"array[number]" : "数组[数字]",
	"number" : "数字",
	"boolean" : "布尔类型"
};

function _alert(msg, fnc) {
	if (fnc) {
		layer.msg(msg, {
			time : 5000
		}, fnc);
	} else {
		layer.msg(msg, {
			time : 5000
		});
	}
}

function _confirm(msg, success) {
	var fnc = function(index) {
		success();
		layer.close(index);
	};
	layer.confirm(msg, {
		icon : 3,
		title : '温馨提示'
	}, fnc);
}

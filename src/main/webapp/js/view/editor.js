var _default_json = {
	"接口名称" : {
		"request" : {
			"requestBody" : {
				"字段名称" : {
					"value" : "字段的值",
					"description" : "字段描述",
					"type" : "字段类型 如：string,number,boolean,array[基础类型], array[object/string/number/boolean], 不填默认为array[string]",
					"require" : "是否可为空 如：true false 不用带引号",
					"rule" : "规则约束"
				}
			}
		},
		"response" : {
			"responseBody" : {
				"字段名称" : {
					"value" : [ {
						"字段名称" : {
							"value" : "字段的值",
							"description" : "字段描述",
							"type" : "字段类型 如：string,number,boolean,array",
							"require" : "是否可为空 如：true false 不用带引号",
							"rule" : "规则约束"
						}
					} ],
					"description" : "字段描述",
					"type" : "array[object]",
					"require" : false,
					"rule" : "规则约束"
				}
			}
		}
	},
	"description" : "接口描述",
	"group" : "所属系统分组 如：BCC,BS等",
	"version" : "1.0.0"
};

var _table_header = "<div class=\"row show-grid table-header\">" + "<div class=\"col-md-2\">名称</div>" + "<div class=\"col-md-1\">类型</div>" + "<div class=\"col-md-2\">是否可为空</div>"
		+ "<div class=\"col-md-2\">描述</div>" + "<div class=\"col-md-2\">约束</div>" + "</div>";
var _json_editor;
var _json_viewer;
$(function() {
	// create the editor
	var container = document.getElementById('jsoneditor');
	_json_editor = new JSONEditor(container);
	// create the viewer
	var viewer = document.getElementById('jsonView');
	var options = {
		"mode" : "text"
	};
	_json_viewer = new JSONEditor(viewer, options);

	_json_editor.set(_default_json);
	_json_editor.expandAll();
	_json_viewer.setText(JSON.stringify(_default_json, null, 2));

	$("#toTree").click(function() {
		var jsonStr = _json_viewer.getText();
		if (jsonStr) {
		try {
			_json_editor.set(eval('(' + jsonStr + ')'));
			_json_editor.expandAll();
		} catch(e) {
			_alert(e.message);
		}
		}
	});

	$("#toText").click(function() {
		var json = _json_editor.get();
		try {
			_json_viewer.setText(JSON.stringify(json, null, 2));
		} catch(e) {
			_alert(e.message);
		}
	});

	var left = (document.documentElement.clientWidth - (1200 + 150)) / 2;
	$("#jsonView").css("margin-left", left);

	var firstKey = initVersionSelect();
	initTree(_tree_nodes[firstKey]);

	initTableSize();

	$(".bt-create").click(function() {
		createDocument(_base_url + "createDocument.do");
	});

	$("#s_version").change(function() {
		initTree(_tree_nodes[$(this).val()]);
	});

	var elem = document.querySelector('.js-switch');
	elem.checked = _isEncode;
	var init = new Switchery(elem);

	elem.onchange = function() {
		var isEncode = this.checked;
		
		$.ajax({
  		url : _base_url + "setting.do",
  		data : {
  			isEncode : isEncode
  		},
  		success : function(data) {
  			if(data.code == 0) {
  				_alert("设置失败！");
  				elem.checked = !isEncode;
  			}
  		}
		});
	};

});

function initVersionSelect() {
	var firstKey = '', i = 0;
	for ( var key in _tree_nodes) {
		$("#s_version").append("<option value='" + key + "'>" + key + "</option>");
		if (i == 0)
			firstKey = key;
		i++;
	}

	return firstKey;
}

function initTree(zNodes) {
	var setting = {
		view : {
			showLine : false,
			selectedMulti : false
		},
		data : {
			simpleData : {
				enable : true
			}
		},
		callback : {
			onClick : findDocument,
			onDblClick : deleteDocument
		}
	};

	$.fn.zTree.init($("#osTree"), setting, zNodes);
}

function initTableSize() {
	var height = document.documentElement.scrollHeight - 635;
	$(".os-tree").css("height", height);
}

function createDocument(url) {
	_confirm("文档生成以右窗口内容为准，是否生成？", function(){
	$.ajax({
		url : url,
		data : {
			jsonStr : function() {
				var str = JSON.stringify(_json_editor.get(), null, 0);
				console.log(str)
				return str;
			}
		},
		success : function(data) {
			if (data.code == _code_success) {
				refreshDocument(data.document);
				_alert("生成成功");
			} else {
				_alert(data.message);
			}
		}
	});
	});
}

function refreshDocument(doc) {
	if (!doc) {
		_alert("文档内容返回为空....");
		return;
	}
	var d_req = doc.request;
	var d_resp = doc.response;
	var d_info = doc.info;

	setInfo(d_info);

	setRequest(d_req);

	setResponse(d_resp);

	initTableSize();

	refreshInterfaceList(d_info);
	var href = window.location.href;
	if (href.indexOf("#icip_table") < 0) {
		href += "#icip_table";
	}
	window.location.href = href;
}

function refreshInterfaceList(info) {
	var itName = info.interfaceName;
	var version = info.version;
	var groupId = info.group;
	var appendGroup = true;
	var appendInterface = true;

	if ($("#s_version option[value='" + version + "']").length > 0) {
		if ($("#s_version option:selected").val() == version) {
		} else {
			$("#s_version option[value='" + version + "']").prop("selected", true);
		}
	} else {
		$("#s_version").append("<option value='" + version + "'>" + version + "</option>");
		$("#s_version option[value='" + version + "']").prop("selected", true);
	}

	var groupList = _tree_nodes[version];
	if (groupList) {
		for (var i = 0; i < groupList.length; i++) {
			var g = groupList[i];
			if(g.pId == 0) {
				if(groupId == g.id) {
					appendGroup = false;
				}
			}
			if (g.name == itName) {
				appendInterface = false;
				break;
			}
		}
		if(appendGroup) {
			groupList.push({
				id : groupId,
				pId : "0",
				name : groupId
			});
		}
	} else {
		groupList = [{
			id : groupId,
			pId : "0",
			name : groupId
		}];
	}
	initTree(groupList);

	var zTree = $.fn.zTree.getZTreeObj("osTree");
	if (appendInterface) {
		var pNode = zTree.getNodeByParam("id", groupId, null);
		var str = {
			id : itName,
			pId : groupId,
			name : itName
		};
		zTree.addNodes(pNode, 0, str);
		groupList.push(str);
		_tree_nodes[version] = groupList;
	}
	var sNode = zTree.getNodeByParam("id", itName, null);
	zTree.selectNode(sNode);
}

function setInfo(d_info) {
	var itName = d_info.interfaceName;
	var index = itName.indexOf("[");
	itName = itName.substring(0, index);
	$("#interfaceName").text(itName);
	$("#group").text(d_info.group);
	$("#it_desc").text(d_info.description);
	$("#it_version").text(d_info.version);
}

function setRequest(d_req) {
	$("#table-request").children().remove();
	$("#table-request").append("<div class=\"jiacu\">请求(request)：</div>");
	$("#table-request").append(createTable(d_req));
}

function setResponse(d_resp) {
	$("#table-response").children().remove();
	$("#table-response").append("<div class=\"jiacu\">返回(response)：</div>")
	$("#table-response").append(createTable(d_resp));
}

function createTable(data) {
	var dom = _table_header;
	$.each(data, function(key, value) {
		var type = value.type;
		var require = value.require ? "否" : "是";
		var description = value.description;
		var rule = value.rule;
		dom += "<div class=\"row show-grid table-content\">"
		dom += "<div class=\"col-md-2\">" + key + "</div>";
		dom += "<div class=\"col-md-1\">" + _type_i18n_cn_ZH[type] + "</div>";
		dom += "<div class=\"col-md-2\">" + require + "</div>";
		dom += "<div class=\"col-md-2\">" + description + "</div>";
		dom += "<div class=\"col-md-2\">" + rule + "</div>";
		dom += "</div>";

		if (type == "array[object]") {
			var column = value.value;
			if (column && column.length > 0) {
				dom += "<div class=\"row show-grid table-content\">";
				dom += "<div class=\"col-md-10\">";
				dom += "<div>" + key + "[0]</div>";
				dom += createTable(column[0]);
				dom += "</div>";
				dom += "</div>";
			}
		}
	});
	return dom;
}

var TimeFn = null;
function findDocument(event, treeId, treeNode, clickFlag) {
  clearTimeout(TimeFn);
  TimeFn = setTimeout(function(){
  	if (!treeNode.pId) {
  		var zTree = $.fn.zTree.getZTreeObj("osTree");
  		zTree.expandNode(treeNode, !treeNode.open, true, true);
  		return;
  	}
  	$.ajax({
  		url : _base_url + "findDoc.do",
  		data : {
  			version : $("#s_version").val(),
  			group : treeNode.pId,
  			interfaceName : treeNode.id
  		},
  		success : function(data) {
  			if (data.code == "1") {
  				refreshDocument(data.document);
  				var json = data.all;
  				if (json) {
  					_json_viewer.setText(JSON.stringify(json, null, 2));
  					_json_editor.set(json);
  					_json_editor.expandAll();
  				}
  				_alert("获取文档成功")
  			} else {
  				_alert("获取文档内容失败")
  			}
  		}
  	});
  },300);
}

function deleteDocument(event, treeId, treeNode) {
	clearTimeout(TimeFn);
	_confirm("是否删除该节点?", function(){
		$.ajax({
			url : _base_url + "deleteDoc.do",
			data : {
				version : $("#s_version").val(),
				group : treeNode.pId,
				interfaceName : treeNode.id
			},
			success : function(data) {
				if(data.code == "1") {
					var zTree = $.fn.zTree.getZTreeObj("osTree");
					zTree.removeNode(treeNode);
					_alert("文档删除成功")
				} else {
					_alert("获取文档内容失败")
				}
			}
		});
	});
}

function showResponseInput() {
	var version = $("#it_version").text();
	var group = $("#group").text();
	var interfaceName = $("#interfaceName").text();
	var desc = $("#it_desc").text();
	if(!version || !group || !interfaceName) {
		_alert("请先选择接口");
		return;
	}
	layer.open({
    type: 2,
    title: '返回报文设置',
    shadeClose: true,
    shade: 0.5,
    area: ['850px', '75%'],
    content: _base_url + "showResponse.ht?version=" + version + "&group=" + group + "&interfaceName=" + interfaceName + "&description=" + desc //iframe的url
	}); 
}
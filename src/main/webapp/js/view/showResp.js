var _json_viewer;
$(function() {
	// create the viewer
	var viewer = document.getElementById('jsonView');
	var options = {
		"mode" : "text"
	};
	_json_viewer = new JSONEditor(viewer, options);

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
});

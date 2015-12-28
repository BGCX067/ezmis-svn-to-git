/**
 * 添加问题反馈
 */
function btnAdd_Click() {
	var obj = {};
	var url = contextPath
			+ '/jteap/index/questionFeedback/questionFeedback.jsp?type=add';
	showIFModule(url, "MIS系统问题反馈", "true", 500, 345, obj);
	rightGrid.getStore().reload();
}

/**
 * 修改问题反馈
 */
function btnModify_Click() {
	var select = rightGrid.getSelectionModel().getSelections()[0];
	if (select) {
		var obj = {};
		// 反馈问题Id
		obj.id = select.data.id;
		obj.createPerson = select.data.createPerson;
		obj.createDate = select.data.createDate;
		obj.content = select.data.content;
		obj.remark = select.data.remark;
		if((curPersonName == obj.createPerson) || (curPersonRoles.indexOf("系统管理员") != -1) || (isRoot)){
			var url = contextPath
					+ '/jteap/index/questionFeedback/questionFeedback.jsp?type=modify';
			showIFModule(url, "MIS系统问题反馈", "true", 500, 345, obj);
			rightGrid.getStore().reload();
		}else{
			alert("你不能修改这条记录!");
			return;
		}
	}else{
		alert("请选择一条记录!");
		return;
	}
}

// 右边的grid
var rightGrid = new RightGrid();

function showChart(obj, num) {
	var surveyId = $("#surveyId").val();
	$("#jqChart" + obj.name + "").css("display", "block");
	$
			.getJSON(
					"Json_selectSurveyResult",
					{
						surveyId : surveyId,
						questionId : obj.name
					},
					function(data) {

						if (num == 1) {
							var arr = [];
							$
									.each(
											data.surveySelectors,
											function(i, value) {
												var selector = [];
												selector[0] = ""
														+ String
																.fromCharCode(64 + parseInt(value.selectorNum))
														+ "";
												selector[1] = value.sumNum;
												arr[i] = selector;
											});
							pieChartForm("#jqChart" + obj.name + "", "", " ",
									arr);
							$("#svgChart" + obj.name + "").val(
									$("#jqChart" + obj.name + "").highcharts()
											.getSVG());
						}
						if (num == 2) {
							var xxx = [];
							var hData = [];// 公共data
							var da = [];// 每次循环生成的一个类型的数据
							$
									.each(
											data.surveySelectors,
											function(i, value) {

												xxx[i] = ""
														+ String
																.fromCharCode(64 + parseInt(value.selectorNum))
														+ "";
												da[i] = value.sumNum;
											});
							combine(hData, "选项统计", da);
							console.log("dfdss" + JSON.stringify(hData));
							histogramChartForm("#jqChart" + obj.name + "", "",
									hData, xxx, "人数");
							$("#svgChart" + obj.name + "").val(
									$("#jqChart" + obj.name + "").highcharts()
											.getSVG());
						}
					});
	$("#img" + obj.name + "").css("display", "none");
	$("#image" + obj.name + "").css("display", "inline");
}
// zq扇形图公用函数
function pieChartForm(elementId, title, dataName, data) {
	var chart1 = new Chart({
		elementId : elementId,
		title : title,
		data : data,
		name : dataName
	});
	chart1.init();
}
// zq扇形图公用函数
function histogramChartForm(elementId, title, data, hx_Axis, hy_title) {
	var hist = new Histogram({
		elementId : elementId,
		title : title,
		subTitle : "",
		data : data,
		hx_Axis : hx_Axis,
		hy_title : hy_title
	});
	hist.init();
}
// zq为生成柱状图拼data
function combine(da, name, arr) {
	var ss = new Object();
	ss.name = name;
	ss.data = arr;
	da.push(ss);
}
// 显示图片
function showDel(obj) {
	$(obj).find("img").css('visibility', 'visible');
}
// 隐藏图片
function hideDel(obj) {
	$(obj).find("img").css('visibility', 'hidden');
}

function hidePie(obj) {
	$("#jqChart" + obj.name + "").css("display", "none");
}
function showPie(obj) {
	$("#jqChart" + obj.name + "").css("display", "block");
}
// 将选项的数字转化未字母
function numChangeToCode() {
	var spans = document.getElementsByClassName("serialNumber");
	for (var i = 0; i < spans.length; i++) {
		spans[i].innerHTML = String.fromCharCode(64 + parseInt(spans[i].id))
				+ "  ."
	}
}
function showTextChart(questionId, page) {
	var surveyId = $("#surveyId").val();
	$("#jqChart" + questionId + "").css("display", "block");
	$("#textAnswerList" + questionId + " tbody").html("");
	$.getJSON("Json_selectSurveyTextResult", {
		page : page,
		surveyId : surveyId,
		questionId : questionId
	}, function(data) {
		$("#page" + questionId + "").html(data.taPageBean.page);
		$("#totalPage" + questionId + "").html(data.taPageBean.totalPage);
		if (data.taPageBean.list.length == 0) {
			alert("未找到相关数据！");
		} else {
			$.each(data.taPageBean.list, function(i, value) {
				$("#textAnswerList" + questionId + "").append(
						"<tr><td class='align_left'>" + value.answerContent
								+ "</td></tr>");
			});
		}

	});
	$("#img" + questionId + "").css("display", "none");
	$("#image" + questionId + "").css("display", "inline");
}

// 上一页
function upPage(obj) {
	var page = parseInt($("#page" + obj.name + "").html());
	if (page == 1) {
		alert("这已经是第一页！");
	} else {
		showTextChart(obj.name, page - 1);
	}
}

// 下一页
function downPage(obj) {
	var totalPage = parseInt($("#totalPage" + obj.name + "").html());
	var page = parseInt($("#page" + obj.name + "").html());
	if (page == totalPage) {
		alert("这已经是最后一页！");
	} else {
		showTextChart(obj.name, page + 1);
	}
}

// 图表题查询统计结果
function showTableChart(obj, num) {
	var surveyId = $("#surveyId").val();
	$("#jqChart" + obj.name + "").css("display", "block");
	$.getJSON("Json_selectSurveyTableResult", {
		surveyId : surveyId,
		questionId : obj.name
	}, function(data) {
		if (num == 4) {
			var da = [];
			var selector = [];
			console.log(data.yaxis);
			console.log("选项" + selector);
			$.each(data.tds, function(i, value) {
				da.push(value);
			});
			console.log(da);
			barChartForm("#jqChart" + obj.name + "", "", "", data.yaxis, "",
					"个", da);
			$("#svgChart" + obj.name + "").val(
					$("#jqChart" + obj.name + "").highcharts().getSVG());
		}
	});
	$("#img" + obj.name + "").css("display", "none");
	$("#image" + obj.name + "").css("display", "inline");
}
function barChartForm(elementId, title, subTitle, hx_Axis, hy_title, unit, data) {
	var bar = new BarChart({
		elementId : elementId,
		title : title,
		subTitle : subTitle,
		hx_Axis : hx_Axis,
		hy_title : hy_title,
		unit : unit,
		data : data
	});
	bar.init();
}
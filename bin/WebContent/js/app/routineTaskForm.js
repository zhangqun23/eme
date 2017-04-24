var app = angular
		.module(
				'routineTaskForm',
				[ 'ngRoute' ],
				function($httpProvider) {// ngRoute引入路由依赖
					$httpProvider.defaults.headers.put['Content-Type'] = 'application/x-www-form-urlencoded';
					$httpProvider.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded';

					// Override $http service's default transformRequest
					$httpProvider.defaults.transformRequest = [ function(data) {
						/**
						 * The workhorse; converts an object to
						 * x-www-form-urlencoded serialization.
						 * 
						 * @param {Object}
						 *            obj
						 * @return {String}
						 */
						var param = function(obj) {
							var query = '';
							var name, value, fullSubName, subName, subValue, innerObj, i;

							for (name in obj) {
								value = obj[name];

								if (value instanceof Array) {
									for (i = 0; i < value.length; ++i) {
										subValue = value[i];
										fullSubName = name + '[' + i + ']';
										innerObj = {};
										innerObj[fullSubName] = subValue;
										query += param(innerObj) + '&';
									}
								} else if (value instanceof Object) {
									for (subName in value) {
										subValue = value[subName];
										fullSubName = name + '[' + subName
												+ ']';
										innerObj = {};
										innerObj[fullSubName] = subValue;
										query += param(innerObj) + '&';
									}
								} else if (value !== undefined
										&& value !== null) {
									query += encodeURIComponent(name) + '='
											+ encodeURIComponent(value) + '&';
								}
							}

							return query.length ? query.substr(0,
									query.length - 1) : query;
						};

						return angular.isObject(data)
								&& String(data) !== '[object File]' ? param(data)
								: data;
					} ];
				});
app.run([ '$rootScope', '$location', function($rootScope, $location) {
	$rootScope.$on('$routeChangeSuccess', function(evt, next, previous) {
		console.log('路由跳转成功');
		$rootScope.$broadcast('reGetData');
	});
} ]);

// 路由配置
app.config([ '$routeProvider', function($routeProvider) {
	$routeProvider.when('/workloadForm', {
		templateUrl : '/HDR/jsp/routineTaskForm/workloadForm.html',
		controller : 'ReportController'
	}).when('/workloadAnalysis', {
		templateUrl : '/HDR/jsp/routineTaskForm/workloadAnalysis.html',
		controller : 'ReportController'
	}).when('/workloadLevelForm', {
		templateUrl : '/HDR/jsp/routineTaskForm/workloadLevelForm.html',
		controller : 'ReportController'
	}).when('/workHouseForm', {
		templateUrl : '/HDR/jsp/routineTaskForm/workHouseForm.html',
		controller : 'ReportController'
	}).when('/workHouseAnalyseForm', {
		templateUrl : '/HDR/jsp/routineTaskForm/workHouseAnalyseForm.html',
		controller : 'ReportController'
	}).when('/workEfficiencyForm', {
		templateUrl : '/HDR/jsp/routineTaskForm/workEfficiencyForm.html',
		controller : 'ReportController'
	}).when('/workEffAnalyseForm', {
		templateUrl : '/HDR/jsp/routineTaskForm/workEffAnalyseForm.html',
		controller : 'ReportController'
	}).when('/workRejectForm', {
		templateUrl : '/HDR/jsp/routineTaskForm/workRejectForm.html',
		controller : 'ReportController'
	}).when('/workRejectAnalyseForm', {
		templateUrl : '/HDR/jsp/routineTaskForm/workRejectAnalyseForm.html',
		controller : 'ReportController'
	}).when('/checkEfficiencyForm', {
		templateUrl : '/HDR/jsp/routineTaskForm/checkEfficiencyForm.html',
		controller : 'ReportController'
	})
} ]);

app.constant('baseUrl', '/HDR/');
app.factory('services', [ '$http', 'baseUrl', function($http, baseUrl) {
	var services = {};
	// zq获取做房用时列表A
	services.selectWorkHouseByLimits = function(data) {
		return $http({
			method : 'post',
			url : baseUrl + 'workHouse/selectWorkHouseBylimits.do',
			data : data
		});
	};
	// zq获取房间类型列表
	services.getRoomSorts = function(data) {
		return $http({
			method : 'post',
			url : baseUrl + 'reportForm/getRoomSorts.do',
			data : data
		});
	};
	// zq获取全体成员
	services.selectRoomStaffs = function(data) {
		return $http({
			method : 'post',
			url : baseUrl + 'reportForm/selectRoomStaffs.do',
			data : data
		});
	};
	// zq获取单个用户的做房用时B
	services.selectUserWorkHouseByLimits = function(data) {
		return $http({
			method : 'post',
			url : baseUrl + 'workHouse/selectUserWorkHouseByLimits.do',
			data : data
		});
	}
	// zq获取做房效率列表A
	services.selectWorkEfficiencyByLimits = function(data) {
		return $http({
			method : 'post',
			url : baseUrl + 'workHouse/selectWorkEfficiencyByLimits.do',
			data : data
		});
	}
	// zq获取个人做房效率B
	services.selectUserWorkEfficiencyByLimits = function(data) {
		return $http({
			method : 'post',
			url : baseUrl + 'workHouse/selectUserWorkEfficiencyByLimits.do',
			data : data
		});
	};

	// zjn例行任务员工工作量或者打扫房间数统计
	services.selectWorkLoadOrRoomByLimits = function(data) {
		return $http({
			method : 'post',
			url : baseUrl + 'workLoad/getWorkLoadOrRoomSummaryList.do',
			data : data
		});
	}

	// lwt例行任务员工工作量分析
	services.selectStaffWorkLoadAnalyse = function(data) {
		return $http({
			method : 'post',
			url : baseUrl + 'workLoad/getStaffWorkLoadAnalyse.do',
			data : data
		});
	}
	// lwt例行任务员工工作量饱和度分析
	services.selectWorkloadLevel = function(data) {
		return $http({
			method : 'post',
			url : baseUrl + 'workLoad/getWorkLoadLevelList.do',
			data : data
		});
	};
	// zq驳回统计
	services.selectWorkRejectByLimits = function(data) {
		return $http({
			method : 'post',
			url : baseUrl + 'workReject/selectWorkRejectByLimits.do',
			data : data
		});
	};
	// zq驳回分析
	services.selectWorkRejectAnalyseByLimits = function(data) {
		return $http({
			method : 'post',
			url : baseUrl + 'workReject/selectWorkRejectAnalyseByLimits.do',
			data : data
		});
	}
	// zq查询领班查房效率
	services.selectCheckEfficiencyByLimits = function(data) {
		return $http({
			method : 'post',
			url : baseUrl + '/checkHouse/getCheckHouseList.do',
			data : data
		});
	};
	return services;
} ]);
app
		.controller(
				'ReportController',
				[
						'$scope',
						'services',
						'$location',
						function($scope, services, $location) {
							var reportForm = $scope;
							var myDate = new Date();
							var myDateTime = new Date().format('yyyy-MM-dd');
							// zq打扫类型默认值
							reportForm.cleanTypes = [ {
								id : 0,
								type : "抹尘房"
							}, {
								id : 1,
								type : "离退房"
							}, {
								id : 2,
								type : "过夜房"
							} ];
							// zq查找时间季度默认值
							reportForm.quarters = [ {
								id : 0,
								type : "全年"
							}, {
								id : 1,
								type : "一季度"
							}, {
								id : 2,
								type : "二季度"
							}, {
								id : 3,
								type : "三季度"
							}, {
								id : 4,
								type : "四季度"
							} ];
							// zq做房用时统计界面设置条件
							reportForm.limit = {
								startTime : myDateTime,
								endTime : myDateTime,
								roomType : "-1"
							};
							// zq做房用时分析界面设置条件
							reportForm.whaLimit = {
								checkYear : myDate.getFullYear(),
								quarter : "0",
								roomType : "-1",
								cleanType : "0",
								staffId : ""
							};
							// zq做房效率统计界面设置条件
							reportForm.wefLimit = {
								startTime : myDateTime,
								endTime : myDateTime
							};
							// zq做房效率分析界面设置条件
							reportForm.weafLimit = {
								checkYear : myDate.getFullYear(),
								quarter : "0",
								staffId : ""
							};

							// 获取房间类型名称
							reportForm.sortName = "";
							// zq驳回率统计
							reportForm.wrLimit = {
								startTime : myDateTime,
								endTime : myDateTime
							}
							// zq驳回率分析折线图条件
							reportForm.wraLimit = {
								checkYear : myDate.getFullYear(),
								quarter : "0",
								cleanType : "0",
								staffId : ""
							}
							// zq公共函数始
							function preventDefault(e) {
								if (e && e.preventDefault) {
									// 阻止默认浏览器动作(W3C)
									e.preventDefault();
								} else {
									// IE中阻止函数器默认动作的方式
									window.event.returnValue = false;
									return false;
								}
							}
							// zq为生成图表拼data
							function combine(da, name, arr) {
								var ss = new Object();
								ss.name = name;
								ss.data = arr;
								da.push(ss);
							}
							// zq公共函数终
							// zq根据条件查找做房时间列表
							reportForm.selectWorkHouseByLimits = function() {
								if (reportForm.limit.startTime == "") {
									alert("请选择开始时间！");
									return false;
								}
								if (reportForm.limit.endTime == "") {
									alert("请选择截止时间！");
									return false;
								}
								if (reportForm.limit.roomType == undefined) {
									alert("请选择房间类型！");
									return false;
								}
								if (reportForm.limit.roomType == "") {
									alert("请选择房间类型！");
									return false;
								}
								if (compareDateTime(reportForm.limit.startTime,
										reportForm.limit.endTime)) {
									alert("截止时间不能大于开始时间！");
									return false;
								}
								$(".overlayer").fadeIn(200);
								$(".tipLoading").fadeIn(200);
								var workHouseLimit = JSON
										.stringify(reportForm.limit);
								services.selectWorkHouseByLimits({
									limit : workHouseLimit
								}).success(function(data) {
									$(".overlayer").fadeOut(200);
									$(".tipLoading").fadeOut(200);
									reportForm.workHouseList = data.list;
									reportForm.remark = data.analyseResult;
									if (data.list.length) {
										reportForm.listIsShow = false;
										reportForm.listRemark = true;
									} else {
										reportForm.listIsShow = true;
										reportForm.listRemark = false;
										reportForm.remark = "";
									}
								});
							}
							// zq根据查询条件绘制员工做房用时分析折线图
							reportForm.selectUserWorkHouseByLimits = function() {
								if (reportForm.whaLimit.checkYear == "") {
									alert("请填写查询年份！");
									return false;
								}
								if (reportForm.whaLimit.roomType == "") {
									alert("请选择房间类型！");
									return false;
								}
								if (reportForm.whaLimit.staffId == "") {
									alert("请选择查询员工！");
									return false;
								}
								$(".overlayer").fadeIn(200);
								$(".tipLoading").fadeIn(200);
								var userWorkHouseLimit = JSON
										.stringify(reportForm.whaLimit);
								services
										.selectUserWorkHouseByLimits({
											limit : userWorkHouseLimit
										})
										.success(
												function(data) {
													$(".overlayer")
															.fadeOut(200);
													$(".tipLoading").fadeOut(
															200);
													var title = "客房员工 "
															+ " "
															+ getSelectedRoomType(reportForm.whaLimit.roomType)
															+ " "
															+ getSelectedCleanType(reportForm.whaLimit.cleanType)
															+ " " + "做房用时分析折线图";// 折线图标题显示
													var xAxis = [];// 横坐标显示
													var yAxis = "做房用时/分钟";// 纵坐标显示
													var nowQuarter = reportForm.whaLimit.quarter;// 当前的选择季度
													var lineName = getSelectedStaff(reportForm.whaLimit.staffId)
															+ "员工做房用时";
													var lineData = [];// 最终传入chart1中的data
													var allAverageData = [];// 全体员工做房时间的平均Data
													var averageData = [];// 个人平均用时
													var userData = [];
													for ( var item in data.list) {
														userData
																.push(changeNumType(data.list[item]));
													}
													switch (nowQuarter) {
													case '0':
														xAxis = [ '1月', '2月',
																'3月', '4月',
																'5月', '6月',
																'7月', '8月',
																'9月', '10月',
																'11月', '12月' ];
														allAverageData = getAverageData(
																changeNumType(data.allAverWorkTime),
																12);
														averageData = getAverageData(
																changeNumType(data.averWorkTime),
																12);
														break;
													case '1':
														xAxis = [ '1月', '2月',
																'3月' ];
														allAverageData = getAverageData(
																changeNumType(data.allAverWorkTime),
																3);
														averageData = getAverageData(
																changeNumType(data.averWorkTime),
																3);
														break;
													case '2':
														xAxis = [ '4月', '5月',
																'6月' ];
														allAverageData = getAverageData(
																changeNumType(data.allAverWorkTime),
																3);
														averageData = getAverageData(
																data.averWorkTime,
																3);
														break;
													case '3':
														xAxis = [ '7月', '8月',
																'9月' ];
														allAverageData = getAverageData(
																changeNumType(data.allAverWorkTime),
																3);
														averageData = getAverageData(
																changeNumType(data.averWorkTime),
																3);
														break;
													case '4':
														xAxis = [ '10月', '11月',
																'12月' ];
														allAverageData = getAverageData(
																changeNumType(data.allAverWorkTime),
																3);
														averageData = getAverageData(
																changeNumType(data.averWorkTime),
																3);
														break;
													}
													combine(lineData, "个人平均用时",
															averageData);
													combine(lineData, "全体平均用时",
															allAverageData);
													combine(lineData, lineName,
															userData);
													lineChartForm(lineData,
															"#lineChart1",
															title, xAxis, yAxis);
													$('#chart1-svg')
															.val(
																	$(
																			"#lineChart1")
																			.highcharts()
																			.getSVG());
													if (data.analyseResult) {
														reportForm.listRemark = true;
														reportForm.remark = data.analyseResult;
														$("#analyseResult")
																.val(
																		data.analyseResult);
													} else {
														reportForm.listRemark = false;
														reportForm.remark = "";
														$("#analyseResult")
																.val("");
													}
												});
							}
							// zq生成平均值的数组
							function getAverageData(data, number) {
								var arr = [];
								for (var i = 0; i < number; i++) {
									arr.push(data);
								}
								return arr;

							}

							// zq换页
							function pageTurn(totalPage, page, Func) {
								var $pages = $(".tcdPageCode");
								if ($pages.length != 0) {
									$(".tcdPageCode").createPage({
										pageCount : totalPage,
										current : page,
										backFn : function(p) {
											Func(p);
										}
									});
								}
							}
							// zq折线图公用函数
							function lineChartForm(data, elementId, title,
									lx_Axis, ly_title) {
								var chart1 = new LineChart({
									elementId : elementId,
									title : title,
									data : data,
									lx_Axis : lx_Axis,
									ly_title : ly_title,
									subTitle : ''
								});
								chart1.init();

							}
							// zq获取房间类型下拉表
							function selectRoomSorts() {
								services.getRoomSorts().success(function(data) {
									reportForm.roomTypes = data.list;
								});
							}
							// zq查询客服人员列表
							function selectRoomStaffs(deptType) {
								services.selectRoomStaffs({
									deptType : deptType
								}).success(function(data) {
									reportForm.staffs = data.list;
								});
							}
							// zq获取所选房间类型
							function getSelectedRoomType(roomSortNo) {
								var type = "";
								for ( var item in reportForm.roomTypes) {
									if (reportForm.roomTypes[item].sortNo == roomSortNo) {
										type = reportForm.roomTypes[item].sortName;
									}
								}
								return type;
							}
							// zq获取所选打扫类型
							function getSelectedCleanType(cleanTypeId) {
								var type = "";
								switch (cleanTypeId) {
								case '0':
									type = "抹尘房";
									break;
								case '1':
									type = "离退房";
									break;
								case '2':
									type = "过夜房";
									break;
								}
								return type;
							}
							// zq获取所选用户
							function getSelectedStaff(staffId) {
								var staffName = "";
								for ( var item in reportForm.staffs) {
									if (reportForm.staffs[item].staff_id == staffId) {
										staffName = reportForm.staffs[item].staff_no
												+ reportForm.staffs[item].staff_name;
									}
								}
								return staffName;
							}

							// zq员工做房效率统计
							reportForm.selectWorkEfficiencyByLimits = function() {
								if (reportForm.wefLimit.startTime == "") {
									alert("请选择开始时间！");
									return false;
								}
								if (reportForm.wefLimit.endTime == "") {
									alert("请选择截止时间！");
									return false;
								}
								if (compareDateTime(
										reportForm.wefLimit.startTime,
										reportForm.wefLimit.endTime)) {
									alert("截止时间不能大于开始时间！");
									return false;
								}
								$(".overlayer").fadeIn(200);
								$(".tipLoading").fadeIn(200);
								var workEfficiencyLimit = JSON
										.stringify(reportForm.wefLimit);
								services.selectWorkEfficiencyByLimits({
									limit : workEfficiencyLimit
								}).success(function(data) {
									$(".overlayer").fadeOut(200);
									$(".tipLoading").fadeOut(200);
									reportForm.workEfficiencyList = data.list;
									reportForm.remark = data.analyseResult;
									if (data.list.length) {
										reportForm.listIsShow = false;
										reportForm.listRemark = true;
									} else {
										reportForm.listIsShow = true;
										reportForm.listRemark = false;
										reportForm.remark = "";
									}
								});

							}
							// zq员工做房效率折线图
							reportForm.selectUserWorkEfficiencyByLimits = function() {
								if (reportForm.weafLimit.checkYear == "") {
									alert("请填写查询年份！");
									return false;
								}
								if (reportForm.weafLimit.quarter == "") {
									alert("请选择查询季度！");
									return false;
								}
								if (reportForm.weafLimit.staffId == "") {
									alert("请选择查询员工！");
									return false;
								}
								$(".overlayer").fadeIn(200);
								$(".tipLoading").fadeIn(200);
								var workEffAnalyseLimit = JSON
										.stringify(reportForm.weafLimit);
								services
										.selectUserWorkEfficiencyByLimits({
											limit : workEffAnalyseLimit
										})
										.success(
												function(data) {
													$(".overlayer")
															.fadeOut(200);
													$(".tipLoading").fadeOut(
															200);
													var title = "图一：客房员工工作效率分析折线图";// 折线图标题显示
													var title1 = "图二：客房员工做房效率分析折线图";
													var xAxis = [];// 横坐标显示
													var yAxis = "效率";// 纵坐标显示
													var nowQuarter = reportForm.weafLimit.quarter;// 当前的选择季度
													var lineName = getSelectedStaff(reportForm.weafLimit.staffId)
															+ "员工工作效率";
													var lineName1 = getSelectedStaff(reportForm.weafLimit.staffId)
															+ "员工做房效率";
													var lineData = [];// 最终传入chart1中的data
													var lineData1 = [];
													var allAverageData = [];// 全体员工工作效率的平均Data
													var averageData = [];// 个人平均工作效率
													var lineData1 = [];// 最终传入chart1中的data
													var allAverageData1 = [];// 全体员工做房效率的平均Data
													var averageData1 = [];// 个人平均做房效率
													var userData = [];// 个人工作效率
													var userData1 = [];// 个人做房效率
													for ( var item in data.list) {
														userData
																.push(changeNumType(data.list[item]));
													}
													for ( var item in data.list1) {
														userData1
																.push(changeNumType(data.list1[item]));
													}
													switch (nowQuarter) {
													case '0':
														xAxis = [ '1月', '2月',
																'3月', '4月',
																'5月', '6月',
																'7月', '8月',
																'9月', '10月',
																'11月', '12月' ];
														allAverageData = getAverageData(
																changeNumType(data.allAverWorkEfficiency),
																12);
														averageData = getAverageData(
																changeNumType(data.averWorkEfficiency),
																12);
														allAverageData1 = getAverageData(
																changeNumType(data.allAverWorkEfficiency1),
																12);
														averageData1 = getAverageData(
																changeNumType(data.averWorkEfficiency1),
																12);
														break;
													case '1':
														xAxis = [ '1月', '2月',
																'3月' ];
														allAverageData = getAverageData(
																changeNumType(data.allAverWorkEfficiency),
																3);
														averageData = getAverageData(
																changeNumType(data.averWorkEfficiency),
																3);
														allAverageData1 = getAverageData(
																changeNumType(data.allAverWorkEfficiency1),
																3);
														averageData1 = getAverageData(
																changeNumType(data.averWorkEfficiency1),
																3);
														break;
													case '2':
														xAxis = [ '4月', '5月',
																'6月' ];
														allAverageData = getAverageData(
																changeNumType(data.allAverWorkEfficiency),
																3);
														averageData = getAverageData(
																changeNumType(data.averWorkEfficiency),
																3);
														allAverageData1 = getAverageData(
																changeNumType(data.allAverWorkEfficiency1),
																3);
														averageData1 = getAverageData(
																changeNumType(data.averWorkEfficiency1),
																3);
														break;
													case '3':
														xAxis = [ '7月', '8月',
																'9月' ];
														allAverageData = getAverageData(
																changeNumType(data.allAverWorkEfficiency),
																3);
														averageData = getAverageData(
																changeNumType(data.averWorkEfficiency),
																3);
														allAverageData1 = getAverageData(
																changeNumType(data.allAverWorkEfficiency1),
																3);
														averageData1 = getAverageData(
																changeNumType(data.averWorkEfficiency1),
																3);
														break;
													case '4':
														xAxis = [ '10月', '11月',
																'12月' ];
														allAverageData = getAverageData(
																changeNumType(data.allAverWorkEfficiency),
																3);
														averageData = getAverageData(
																changeNumType(data.averWorkEfficiency),
																3);
														allAverageData1 = getAverageData(
																changeNumType(data.allAverWorkEfficiency1),
																3);
														averageData1 = getAverageData(
																changeNumType(data.averWorkEfficiency1),
																3);
														break;
													}

													combine(lineData,
															"个人平均工作效率",
															averageData);
													combine(lineData,
															"全体平均工作效率",
															allAverageData);
													combine(lineData, lineName,
															userData);
													combine(lineData1,
															"个人平均做房效率",
															averageData1);
													combine(lineData1,
															"全体平均做房效率",
															allAverageData1);
													combine(lineData1,
															lineName1,
															userData1);
													lineChartForm(lineData,
															"#lineChart",
															title, xAxis, yAxis);
													lineChartForm(lineData1,
															"#lineChart1",
															title1, xAxis,
															yAxis);
													$('#chart-svg')
															.val(
																	$(
																			"#lineChart")
																			.highcharts()
																			.getSVG());
													$('#chart1-svg')
															.val(
																	$(
																			"#lineChart1")
																			.highcharts()
																			.getSVG());
													if (data.analyseResult1
															|| data.analyseResult2) {
														reportForm.listRemark = true;
														reportForm.remark1 = data.analyseResult1;
														reportForm.remark2 = data.analyseResult2;
														$("#analyseResult1")
																.val(
																		data.analyseResult1);
														$("#analyseResult2")
																.val(
																		data.analyseResult2);
													} else {
														reportForm.listRemark = false;
														reportForm.remark1 = "";
														reportForm.remark2 = "";
														$("#analyseResult1")
																.val("");
														$("#analyseResult2")
																.val("");
													}
												});
								if (data.analyseResult) {
									reportForm.listRemark = true;
									reportForm.remark = data.analyseResult;
									$("#analyseResult").val(data.analyseResult);
								} else {
									reportForm.listRemark = false;
									reportForm.remark = "";
									$("#analyseResult").val("");
								}
							}
							// zq比较两个时间的大小
							function compareDateTime(startDate, endDate) {
								var date1 = new Date(startDate);
								var date2 = new Date(endDate);
								if (date2.getTime() < date1.getTime()) {
									return true;
								} else {
									return false;
								}
							}
							// zq当房型下拉框变化时获取房型名字
							reportForm.getSortNameByNo = function() {
								var no = $("#roomSortType").val();
								reportForm.sortName = getSelectedRoomType(no);
							}

							// lwt例行任务工作量统计界面设置条件
							reportForm.workloadLimit = {
								tableType : "0",
								startTime : myDateTime,
								endTime : myDateTime
							};
							// lwt例行任务工作量分析界面设置条件
							reportForm.staffWorkloadLimit = {
								checkYear : myDate.getFullYear(),
								quarter : "0",
								staffId : ""
							};
							// lwt例行任务工作量饱和度分析设置条件
							reportForm.workLoadLevelLimit = {
								startTime : myDateTime,
								endTime : myDateTime
							};
							// lwt根据条件查找例行任务工作量统计
							reportForm.selectWorkloadByLimits = function() {
								if (reportForm.workloadLimit.startTime == "") {
									alert("请选择开始时间！");
									return false;
								}
								if (reportForm.workloadLimit.endTime == "") {
									alert("请选择截止时间！");
									return false;
								}
								if (compareDateTime(
										reportForm.workloadLimit.startTime,
										reportForm.workloadLimit.endTime)) {
									alert("截止时间不能大于开始时间！");
									return false;
								}
								if (reportForm.workloadLimit.tableType == "") {
									alert("请选择统计类型！");
									return false;
								}
								$(".overlayer").fadeIn(200);
								$(".tipLoading").fadeIn(200);
								var workloadLimit = JSON
										.stringify(reportForm.workloadLimit);
								services
										.selectWorkLoadOrRoomByLimits(
												{
													tableType : reportForm.workloadLimit.tableType,
													startDate : reportForm.workloadLimit.startTime,
													endDate : reportForm.workloadLimit.endTime
												})
										.success(
												function(data) {
													$(".overlayer")
															.fadeOut(200);
													$(".tipLoading").fadeOut(
															200);
													if (reportForm.workloadLimit.tableType == '0') {
														reportForm.workloadList = data.workLoadList;
													} else {
														reportForm.workRoomList = data.workLoadList;
													}
													reportForm.remark = data.analyseResult;
													if (data.workLoadList.length) {
														reportForm.listIsShow = false;
														reportForm.listRemark = true;
													} else {
														reportForm.listIsShow = true;
														reportForm.listRemark = false;
														reportForm.remark = "";
													}
												});
							}
							// zjn根据选择的报表类型显示不同的报表
							reportForm.changeTable = function() {
								var table = $("#tableType").val();
								if (table == '0') {
									$("#workLoadTable").show();
									$("#workRoomNumTable").hide();

								} else {
									$("#workLoadTable").hide();
									$("#workRoomNumTable").show();
								}
							}
							// lwt根据条件分析员工的工作量
							reportForm.selectStaffWorkLoadAnalyse = function() {
								if (reportForm.staffWorkloadLimit.checkYear == "") {
									alert("请填写年份！");
									return false;
								}
								if (reportForm.staffWorkloadLimit.staffId == "") {
									alert("请选择员工！");
									return false;
								}
								$(".overlayer").fadeIn(200);
								$(".tipLoading").fadeIn(200);
								var staffWorkloadLimit = JSON
										.stringify(reportForm.staffWorkloadLimit);
								services
										.selectStaffWorkLoadAnalyse(
												{
													// limit :
													// staffWorkloadLimit
													checkYear : reportForm.staffWorkloadLimit.checkYear,
													quarter : reportForm.staffWorkloadLimit.quarter,
													staffId : reportForm.staffWorkloadLimit.staffId
												})
										.success(
												function(data) {
													$(".overlayer")
															.fadeOut(200);
													$(".tipLoading").fadeOut(
															200);
													var xAxis = [];// 横坐标显示
													var yAxis = "工作量";// 纵坐标显示
													var nowQuarter = reportForm.staffWorkloadLimit.quarter;// 当前的选择季度
													var nowQuarterName = "";
													var lineData = [];// 最终传入chart1中的data
													var allAverageData = [];// 全体员工工作量的平均Data
													var averageData = [];// 个人平均工作量
													var staffData = [];// 员工工作量
													var ratedData = [];// 额定工作量

													for ( var item in data.workLoadMonths) {
														staffData
																.push(changeNumType(data.workLoadMonths[item].actualLoad));
													}
													for ( var item in data.workLoadMonths) {
														ratedData
																.push(changeNumType(data.workLoadMonths[item].ratedLoad));
													}
													switch (nowQuarter) {
													case '0':
														xAxis = [ '1月', '2月',
																'3月', '4月',
																'5月', '6月',
																'7月', '8月',
																'9月', '10月',
																'11月', '12月' ];
														nowQuarterName = "全年";
														allAverageData = getAverageData(
																changeNumType(data.allAverageData),
																12);
														averageData = getAverageData(
																changeNumType(data.averageData),
																12);
														break;
													case '1':
														xAxis = [ '1月', '2月',
																'3月' ];
														nowQuarterName = "第一季度";
														allAverageData = getAverageData(
																changeNumType(data.allAverageData),
																3);
														averageData = getAverageData(
																changeNumType(data.averageData),
																3);
														break;
													case '2':
														xAxis = [ '4月', '5月',
																'6月' ];
														nowQuarterName = "第二季度";
														allAverageData = getAverageData(
																changeNumType(data.allAverageData),
																3);
														averageData = getAverageData(
																changeNumType(data.averageData),
																3);
														break;
													case '3':
														xAxis = [ '7月', '8月',
																'9月' ];
														nowQuarterName = "第三季度";
														allAverageData = getAverageData(
																changeNumType(data.allAverageData),
																3);
														averageData = getAverageData(
																changeNumType(data.averageData),
																3);
														break;
													case '4':
														xAxis = [ '10月', '11月',
																'12月' ];
														nowQuarterName = "第四季度";
														allAverageData = getAverageData(
																changeNumType(data.allAverageData),
																3);
														averageData = getAverageData(
																changeNumType(data.averageData),
																3);
														break;
													}
													var title = "员工 "
															+ "("
															+ getSelectedStaff(reportForm.staffWorkloadLimit.staffId)
															+ ")"
															+ (reportForm.staffWorkloadLimit.checkYear)
															+ nowQuarterName
															+ "工作量动态变化趋势图";// 折线图标题显示

													combine(lineData, "员工工作量",
															staffData);
													combine(lineData, "额定工作量",
															ratedData);
													combine(lineData,
															"全体平均工作量",
															allAverageData);
													combine(lineData,
															"个人平均工作量",
															averageData);

													lineChartForm(lineData,
															"#lineChart1",
															title, xAxis, yAxis);
													$('#chart1-svg')
															.val(
																	$(
																			"#lineChart1")
																			.highcharts()
																			.getSVG());
													reportForm.remark = data.analyseResult;
													$("#analyseResult").val(
															data.analyseResult);
													if (data.analyseResult) {
														reportForm.listIsShow = false;
														reportForm.listRemark = true;
													} else {
														reportForm.listIsShow = true;
														reportForm.listRemark = false;
													}
												});
							}
							// lwt例行任务工作量饱和度分析
							reportForm.selectWorkloadLevel = function() {
								if (reportForm.workLoadLevelLimit.startTime == "") {
									alert("请选择开始时间！");
									return false;
								}
								if (reportForm.workLoadLevelLimit.endTime == "") {
									alert("请选择截止时间！");
									return false;
								}
								if (compareDateTime(
										reportForm.workLoadLevelLimit.startTime,
										reportForm.workLoadLevelLimit.endTime)) {
									alert("截止时间不能大于开始时间！");
									return false;
								}
								$(".overlayer").fadeIn(200);
								$(".tipLoading").fadeIn(200);
								var workLoadLevelLimit = JSON
										.stringify(reportForm.workLoadLevelLimit);
								services
										.selectWorkloadLevel(
												{
													startDate : reportForm.workLoadLevelLimit.startTime,
													endDate : reportForm.workLoadLevelLimit.endTime
												})
										.success(
												function(data) {
													$(".overlayer")
															.fadeOut(200);
													$(".tipLoading").fadeOut(
															200);
													reportForm.workloadLevels = data.WorkLoadLevelList;
													reportForm.remark = data.analyseResult;
													if (data.WorkLoadLevelList.length) {
														reportForm.listIsShow = false;
														reportForm.listRemark = true;
													} else {
														reportForm.listIsShow = true;
														reportForm.listRemark = false;
														reportForm.remark = "";
													}
												});
							}
							// zq做房驳回率统计
							reportForm.selectWorkRejectByLimits = function() {
								if (reportForm.wrLimit.startTime == "") {
									alert("请选择起始时间！");
									return false;
								}
								if (reportForm.wrLimit.endTime == "") {
									alert("请选择截止时间！");
									return false;
								}
								$(".overlayer").fadeIn(200);
								$(".tipLoading").fadeIn(200);
								var workRejectLimit = JSON
										.stringify(reportForm.wrLimit);
								console.log(workRejectLimit);
								services.selectWorkRejectByLimits({
									limit : workRejectLimit
								}).success(function(data) {
									$(".overlayer").fadeOut(200);
									$(".tipLoading").fadeOut(200);
									reportForm.workRejectList = data.list;
									reportForm.remark = data.analyseResult;
									if (data.list.length) {
										reportForm.listIsShow = false;
										reportForm.listRemark = true;
									} else {
										reportForm.listIsShow = true;
										reportForm.listRemark = false;
										reportForm.remark = "";
									}
								});
							}
							// zq做房驳回率折线图扇形图
							reportForm.selectWorkRejectAnalyseByLimits = function() {
								if (reportForm.wraLimit.checkYear == "") {
									alert("请填写查询年份！");
									return false;
								}
								if (reportForm.wraLimit.quarter == "") {
									alert("请选择查询季度！");
									return false;
								}
								if (reportForm.wraLimit.staffId == "") {
									alert("请选择查询员工！");
									return false;
								}
								if (reportForm.wraLimit.cleanType == "") {
									alert("请选择打扫类型！");
									return false;
								}
								$(".overlayer").fadeIn(200);
								$(".tipLoading").fadeIn(200);
								var workRejectAnalyseLimit = JSON
										.stringify(reportForm.wraLimit);
								console.log(workRejectAnalyseLimit);
								services
										.selectWorkRejectAnalyseByLimits({
											limit : workRejectAnalyseLimit
										})
										.success(
												function(data) {
													$(".overlayer")
															.fadeOut(200);
													$(".tipLoading").fadeOut(
															200);
													var title = "员工（"
															+ getSelectedStaff(reportForm.wraLimit.staffId)
															+ "）"
															+ reportForm.wraLimit.checkYear
															+ "年度  "
															+ getSelectedQuarter(reportForm.wraLimit.quarter)
															+ "  做房驳回率趋势图";// 折线图标题显示
													var xAxis = [];// 横坐标显示
													var yAxis = "效率";// 纵坐标显示
													var nowQuarter = reportForm.wraLimit.quarter;// 当前的选择季度
													var lineName = getSelectedStaff(reportForm.wraLimit.staffId)
															+ "员工单月做房驳回率";
													var lineData = [];// 最终传入chart1中的data
													var allAverageData = [];// 全体员工做房驳回率的平均Data
													var averageData = [];// 个人平均做房驳回率
													var userData = [];// 个人工作效率
													var singleMonthAllData = [];// 每个月的全体平均工作率
													for ( var item in data.list) {
														userData
																.push(changeNumType(data.list[item]));
													}
													for ( var key in data.allMonAveListStr) {
														singleMonthAllData
																.push(changeNumType(data.allMonAveListStr[key]));
													}
													switch (nowQuarter) {
													case '0':
														xAxis = [ '1月', '2月',
																'3月', '4月',
																'5月', '6月',
																'7月', '8月',
																'9月', '10月',
																'11月', '12月' ];
														allAverageData = getAverageData(
																changeNumType(data.allAverRejectEff),
																12);
														averageData = getAverageData(
																changeNumType(data.averRejectEff),
																12);

														break;
													case '1':
														xAxis = [ '1月', '2月',
																'3月' ];
														allAverageData = getAverageData(
																changeNumType(data.allAverRejectEff),
																3);
														averageData = getAverageData(
																changeNumType(data.averRejectEff),
																3);

														break;
													case '2':
														xAxis = [ '4月', '5月',
																'6月' ];
														allAverageData = getAverageData(
																changeNumType(data.allAverRejectEff),
																3);
														averageData = getAverageData(
																data.averRejectEff,
																3);
														break;
													case '3':
														xAxis = [ '7月', '8月',
																'9月' ];
														allAverageData = getAverageData(
																changeNumType(data.allAverRejectEff),
																3);
														averageData = getAverageData(
																changeNumType(data.averRejectEff),
																3);
														break;
													case '4':
														xAxis = [ '10月', '11月',
																'12月' ];
														allAverageData = getAverageData(
																changeNumType(data.allAverRejectEff),
																3);
														averageData = getAverageData(
																changeNumType(data.averRejectEff),
																3);
														break;
													}

													combine(lineData,
															"个人平均做房驳回率",
															averageData);
													combine(lineData,
															"全体平均做房驳回率",
															allAverageData);
													combine(lineData, lineName,
															userData);
													combine(lineData,
															"单月全体平均驳回率",
															singleMonthAllData);

													lineChartForm(lineData,
															"#lineChart",
															title, xAxis, yAxis);

													$('#chart-svg')
															.val(
																	$(
																			"#lineChart")
																			.highcharts()
																			.getSVG());
													var title1 = "驳回原因汇总";
													var pieReasons = [];
													for (var i = 0; i < data.reasonList.length; i++) {
														var s = [];
														switch (i) {
														case 0:
															s = [
																	'布草问题',
																	data.reasonList[0] ];
															break;
														case 1:
															s = [
																	'迷你吧问题',
																	data.reasonList[1] ];
															break;
														case 2:
															s = [
																	'卫生间问题',
																	data.reasonList[2] ];
															break;
														case 3:
															s = [
																	'毛巾问题',
																	data.reasonList[3] ];
															break;
														case 4:
															s = [
																	'房间卫生',
																	data.reasonList[4] ];
															break;
														case 5:
															s = [
																	'其他',
																	data.reasonList[5] ];
															break;

														}
														pieReasons.push(s);
													}
													pieChartForm("#pieChart",
															title1, "做房驳回原因",
															pieReasons);
													$('#chart1-svg')
															.val(
																	$(
																			"#pieChart")
																			.highcharts()
																			.getSVG());
													if (data.analyseResult) {
														reportForm.listRemark = true;
														reportForm.remark = data.analyseResult;
														$("#analyseResult")
																.val(
																		data.analyseResult);
													} else {
														reportForm.listRemark = false;
														reportForm.remark = "";
														$("#analyseResult")
																.val("");
													}
												});
							}
							// zq扇形图公用函数
							function pieChartForm(elementId, title, dataName,
									data) {
								var chart1 = new Chart({
									elementId : elementId,
									title : title,
									data : data,
									name : dataName
								});
								chart1.init();
							}
							// zq将小数保留两位小数
							function changeNumType(number) {
								if (!number) {
									var defaultNum = 0;
									var num = parseFloat(parseFloat(defaultNum)
											.toFixed(2));
								} else {
									var num = parseFloat(parseFloat(number)
											.toFixed(2));
								}
								return num;
							}
							// zq获取下拉框得到的员工姓名
							reportForm.staffName = "";
							reportForm.getStaffNameById = function() {
								var name = $("#staffId").val();
								reportForm.staffName = getSelectedStaff(name);
							}
							function getSelectedQuarter(id) {
								var qName = "";
								switch (id) {
								case '0':
									qName = "全年";
									break;
								case '1':
									qName = "第一季度";
									break;
								case '2':
									qName = "第二季度";
									break;
								case '3':
									qName = "第三季度";
									break;
								case '4':
									qName = "第四季度";
									break;
								}
								return qName;
							}
							// zq获取领班查房效率列表
							// zq查房效率
							reportForm.ceLimit = {
								startTime : myDateTime,
								endTime : myDateTime
							}
							reportForm.selectCheckEfficiencyByLimits = function() {
								if (reportForm.ceLimit.startTime == "") {
									alert("请选择起始时间！");
									return false;
								}
								if (reportForm.ceLimit.endTime == "") {
									alert("请选择截止时间！");
									return false;
								}
								$(".overlayer").fadeIn(200);
								$(".tipLoading").fadeIn(200);
								services
										.selectCheckEfficiencyByLimits(
												{
													startTime : reportForm.ceLimit.startTime,
													endTime : reportForm.ceLimit.endTime
												})
										.success(
												function(data) {
													$(".overlayer")
															.fadeOut(200);
													$(".tipLoading").fadeOut(
															200);
													reportForm.checkEfficiencyList = data.checkHouseList;

													reportForm.remark = data.analyseResult;
													if (data.checkHouseList.length) {
														reportForm.listIsShow = false;
														reportForm.listRemark = true;
													} else {
														reportForm.listIsShow = true;
														reportForm.listRemark = false;
														reportForm.remark = "";
													}
												});
							}
							// zq初始化
							function initData() {
								console.log("初始化页面信息");
								Highcharts
										.wrap(
												Highcharts.Chart.prototype,
												'getSVG',
												function(proceed) {
													return proceed
															.call(this)
															.replace(
																	/(fill|stroke)="rgba([ 0-9]+,[ 0-9]+,[ 0-9]+),([ 0-9\.]+)"/g,
																	'$1="rgb($2)" $1-opacity="$3"');
												});
								if ($location.path().indexOf('/workHouseForm') == 0) {
									selectRoomSorts();

								} else if ($location.path().indexOf(
										'/workHouseAnalyseForm') == 0) {
									selectRoomStaffs(0);
									selectRoomSorts();

								} else if ($location.path().indexOf(
										'/reportForm') == 0) {

								} else if ($location.path().indexOf(
										'/workEfficiencyForm') == 0) {

								} else if ($location.path().indexOf(
										'/workEffAnalyseForm') == 0) {
									selectRoomStaffs(0);
									selectRoomSorts();
								} else if ($location.path().indexOf(
										'/workloadAnalysis') == 0) {
									selectRoomStaffs(0);
								} else if ($location.path().indexOf(
										'/workRejectForm') == 0) {

								} else if ($location.path().indexOf(
										'/workRejectAnalyseForm') == 0) {
									selectRoomStaffs(0);
								} else if ($location.path().indexOf(
										'/checkEfficiencyForm') == 0) {
									selectRoomSorts();
								}
							}
							initData();
							// zq控制年
							var $dateFormat = $(".dateFormatForY");
							var dateRegexpForY = /^[0-9]{4}$/;
							$(".dateFormatForY").blur(
									function() {
										if (this.value.trim() != "") {
											if (!dateRegexpForY
													.test(this.value)) {
												$(this).parent().children(
														"span").css('display',
														'inline');
											} else {
												var month = parseInt(this.value
														.split("-")[1]);
												if (month > 12) {
													$(this).parent().children(
															"span")
															.css('display',
																	'inline');
												}
											}
										}
									});
							$(".dateFormatForY").click(
									function() {
										$(this).parent().children("span").css(
												'display', 'none');
									});

						} ]);

// 小数过滤器
app.filter('numFloat', function() {
	return function(input) {
		if (!input) {
			var number = parseFloat('0').toFixed(2);
		} else {
			var number = parseFloat(input).toFixed(2);
		}
		return number;
	}
});

// lwt:小数转换为百分数过滤器
app.filter('numPercent', function() {
	return function(input) {
		var number = (input * 100).toFixed(2) + "%";
		return number;
	}
});

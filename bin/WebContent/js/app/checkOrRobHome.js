var app = angular
		.module(
				'checkOrRobHome',
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
	$routeProvider.when('/robEfficiencyForm', {
		templateUrl : '/HDR/jsp/checkOrRobHome/robEfficiencyForm.html',
		controller : 'CheckOrRobHomeController'
	}).when('/robEffAnalyseForm', {
		templateUrl : '/HDR/jsp/checkOrRobHome/robEffAnalyseForm.html',
		controller : 'CheckOrRobHomeController'
	}).when('/checkOutHomeForm', {
		templateUrl : '/HDR/jsp/checkOrRobHome/checkOutHomeForm.html',
		controller : 'CheckOrRobHomeController'
	}).when('/checkOutAnalyseForm', {
		templateUrl : '/HDR/jsp/checkOrRobHome/checkOutAnalyseForm.html',
		controller : 'CheckOrRobHomeController'
	})
} ]);

app.constant('baseUrl', '/HDR/');
app
		.factory(
				'services',
				[
						'$http',
						'baseUrl',
						function($http, baseUrl) {
							var services = {};
							// zq获取抢房效率列表
							services.selectRobEfficiencyByLimits = function(
									data) {
								return $http({
									method : 'post',
									url : baseUrl
											+ 'checkOrRobHome/selectRobEfficiencyByLimits.do',
									data : data
								});
							};
							// zq获取房间类型列表
							services.getRoomSorts = function(data) {
								return $http({
									method : 'post',
									url : baseUrl
											+ 'reportForm/getRoomSorts.do',
									data : data
								});
							};
							// zq获取全体成员
							services.selectRoomStaffs = function(data) {
								return $http({
									method : 'post',
									url : baseUrl
											+ 'reportForm/selectRoomStaffs.do',
									data : data
								});
							};
							// zq获取抢房明细列表
							services.selectRobDetailByLimits = function(data) {
								return $http({
									method : 'post',
									url : baseUrl
											+ 'checkOrRobHome/selectRobDetailByLimits.do',
									data : data
								});
							}
							// zq获取个人抢房工作效率折线图
							services.selectRobEffAnalyseByLimits = function(
									data) {
								return $http({
									method : 'post',
									url : baseUrl
											+ 'checkOrRobHome/selectRobEffAnalyseByLimits.do',
									data : data
								});
							};

							// zq获取查退房效率表
							services.selectCheckOutEfficiencyByLimits = function(
									data) {
								return $http({
									method : 'post',
									url : baseUrl
											+ '/checkOutHome/selectCheckOutEfficiency.do',
									data : data
								});
							};
							// zq查退房明细表
							services.selectCheckOutDetailByLimits = function(
									data) {
								return $http({
									method : 'post',
									url : baseUrl
											+ '/checkOutHome/selectCheckOutDetailByLimits.do',
									data : data
								});
							};
							// zq查退房效率分析图
							services.selectCheckOutAnalyseByLimits = function(
									data) {
								return $http({
									method : 'post',
									url : baseUrl
											+ '/checkOutHome/selectCheckOutEffAnalyseByLimits.do',
									data : data
								});
							};
							return services;
						} ]);
app
		.controller(
				'CheckOrRobHomeController',
				[
						'$scope',
						'services',
						'$location',
						function($scope, services, $location) {
							var checkRob = $scope;
							var nowPage = 1;
							var myDate = new Date();
							var myDateTime=new Date().format('yyyy-MM-dd');
							// zq打扫类型默认值
							checkRob.cleanTypes = [ {
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
							checkRob.quarters = [ {
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
							// 抢房效率统计查询限制条件
							checkRob.reLimit = {
								tableType : "0",
								startTime : myDateTime,
								endTime : myDateTime,
								roomType : "-1"
							}
							// 抢房效率分析查询限制条件
							checkRob.reaLimit = {
								checkYear : myDate.getFullYear(),
								quarter : "0",
								roomType : "-1",
								staffId : ""
							}
							// 查退房效率统计查询限制条件
							checkRob.coLimit = {
								tableType : "0",
								startTime : myDateTime,
								endTime : myDateTime,
								roomType : "-1"
							}
							// 获取房间类型名称
							checkRob.sortName = "";
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
											nowPage = p;// 暂时没用，留待将来换页改序号使用
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
									checkRob.roomTypes = data.list;
								});
							}
							// zq查询客服人员列表
							function selectRoomStaffs(deptType) {
								services.selectRoomStaffs({
									deptType : deptType
								}).success(function(data) {
									checkRob.staffs = data.list;
								});
							}
							// zq获取所选房间类型
							function getSelectedRoomType(roomSortNo) {
								var type = "";
								for ( var item in checkRob.roomTypes) {
									if (checkRob.roomTypes[item].sortNo == roomSortNo) {
										type = checkRob.roomTypes[item].sortName;
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
								for ( var item in checkRob.staffs) {
									if (checkRob.staffs[item].staff_id == staffId) {
										staffName = checkRob.staffs[item].staff_no
												+ checkRob.staffs[item].staff_name;
									}
								}
								return staffName;
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
							checkRob.getSortNameByNo = function() {
								var no = $("#roomSortType").val();
								if (no == '-1') {
									checkRob.sortName = "全部类型";
								} else {
									checkRob.sortName = getSelectedRoomType(no);
								}

							}
							// zq查询抢房明细表
							checkRob.selectRobByLimits = function() {
								if (checkRob.reLimit.startTime == "") {
									alert("请选择开始时间！");
									return false;
								}
								if (checkRob.reLimit.endTime == "") {
									alert("请选择截止时间！");
									return false;
								}
								if (checkRob.reLimit.roomType == "") {
									alert("请选择房间类型！");
									return false;
								}
								$(".overlayer").fadeIn(200);
								$(".tipLoading").fadeIn(200);
								robEfficiencyLimit = JSON
										.stringify(checkRob.reLimit);
								if (checkRob.reLimit.tableType == '0') {
									services.selectRobEfficiencyByLimits({
										limit : robEfficiencyLimit,
										page : 1
									}).success(function(data) {
										$(".overlayer").fadeOut(200);
										$(".tipLoading").fadeOut(200);
										checkRob.robEfficiencyList = data.list;
										checkRob.remark = data.analyseResult;
										if (data.list.length) {
											checkRob.listIsShow = false;
											checkRob.listRemark = true;
										} else {
											checkRob.listIsShow = true;
											checkRob.listRemark = false;
											checkRob.remark = "";
										}
									});
								} else {
									services
											.selectRobDetailByLimits({
												limit : robEfficiencyLimit,
												page : 1
											})
											.success(
													function(data) {
														$(".overlayer")
																.fadeOut(200);
														$(".tipLoading")
																.fadeOut(200);
														checkRob.robDetailList = data.list;
														console
																.log("ddsjhfkdshfkjdshfkhsdkfh"
																		+ data.list.length);
														pageTurn(
																data.totalPage,
																1,
																getRobDetailByLimits);
														checkRob.remark = data.analyseResult;
														if (data.list.length) {
															checkRob.listIsShow = false;
															checkRob.listRemark = true;
														} else {
															checkRob.listIsShow = true;
															checkRob.listRemark = false;
															checkRob.remark = "";
														}
													});
								}

							}
							// zq抢房明细表换页函数
							function getRobDetailByLimits(p) {
								services.selectRobDetailByLimits({
									limit : robEfficiencyLimit,
									page : p
								}).success(function(data) {
									checkRob.robDetailList = data.list;
								});
							}

							// zq抢房效率折线图
							checkRob.selectRobEffAnalyseByLimits = function() {
								if (checkRob.reaLimit.checkYear == "") {
									alert("请填写查询年份！");
									return false;
								}
								if (checkRob.reaLimit.roomType == "") {
									alert("请选择房间类型！");
									return false;
								}
								if (checkRob.reaLimit.staffId == "") {
									alert("请选择查询员工！");
									return false;
								}
								$(".overlayer").fadeIn(200);
								$(".tipLoading").fadeIn(200);
								var robEffAnalyseLimit = JSON
										.stringify(checkRob.reaLimit);
								services
										.selectRobEffAnalyseByLimits({
											limit : robEffAnalyseLimit
										})
										.success(
												function(data) {
													$(".overlayer")
															.fadeOut(200);
													$(".tipLoading").fadeOut(
															200);
													var title = "客房员工 "
															+ " "
															+ getSelectedRoomType(checkRob.reaLimit.roomType)
															+ " " + "抢房用时分析折线图";// 折线图标题显示
													var xAxis = [];// 横坐标显示
													var yAxis = "抢房用时";// 纵坐标显示
													var nowQuarter = checkRob.reaLimit.quarter;// 当前的选择季度
													var lineName = getSelectedStaff(checkRob.reaLimit.staffId)
															+ "员工抢房用时";
													var lineData = [];// 最终传入chart1中的data
													var allAverageData = [];// 全体员工抢房效率的平均Data
													var averageData = [];// 个人平均抢房效率
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
																changeNumType(data.allAverWorkEfficiency),
																12);
														averageData = getAverageData(
																changeNumType(data.averWorkEfficiency),
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
														break;
													}
													combine(lineData,
															"个人平均抢房用时",
															averageData);
													combine(lineData,
															"全体平均抢房用时",
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
														checkRob.listRemark = true;
														checkRob.remark = data.analyseResult;
														$("#analyseResult")
																.val(
																		data.analyseResult);
													} else {
														checkRob.listRemark = false;
														checkRob.remark = "";
														$("#analyseResult")
																.val("");
													}
												});
							}
							// zq根据选择的报表类型显示不同的报表
							checkRob.changeTable = function() {
								var table = $("#tableType").val();
								if (table == '0') {
									$("#efficiencyTable").show();
									$("#detailTable").hide();

								} else {
									$("#efficiencyTable").hide();
									$("#detailTable").show();
								}
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
							checkRob.staffName = "";
							checkRob.getStaffNameById = function() {
								var name = $("#staffId").val();
								checkRob.staffName = getSelectedStaff(name);
							}

							// zq获取查退房明细或效率表selectCheckOutRooms
							checkRob.selectCheckOutRooms = function() {

								if (checkRob.coLimit.startTime == "") {
									alert("请选择开始时间！");
									return false;
								}
								if (checkRob.coLimit.endTime == "") {
									alert("请选择截止时间！");
									return false;
								}
								if (checkRob.coLimit.roomType == undefined) {
									alert("请选择房间类型！");
									return false;
								}
								if (checkRob.coLimit.roomType == "") {
									alert("请选择房间类型！");
									return false;
								}
								$(".overlayer").fadeIn(200);
								$(".tipLoading").fadeIn(200);
								checkOutLimit = JSON
										.stringify(checkRob.coLimit);
								if (checkRob.coLimit.tableType == '0') {
									services
											.selectCheckOutEfficiencyByLimits({
												limit : checkOutLimit,
												page : 1
											})
											.success(
													function(data) {
														$(".overlayer")
																.fadeOut(200);
														$(".tipLoading")
																.fadeOut(200);
														checkRob.checkOutEfficiencyList = data.list;
														checkRob.remark = data.analyseResult;
														if (data.list.length) {
															checkRob.listIsShow = false;
															checkRob.listRemark = true;
														} else {
															checkRob.listIsShow = true;
															checkRob.listRemark = false;
															checkRob.remark = "";
														}
													});
								} else {
									services
											.selectCheckOutDetailByLimits({
												limit : checkOutLimit,
												page : 1
											})
											.success(
													function(data) {
														$(".overlayer")
																.fadeOut(200);
														$(".tipLoading")
																.fadeOut(200);
														checkRob.CheckOutDetailList = data.list;
														pageTurn(
																data.totalPage,
																1,
																getCheckOutDetailByLimits);
														if (data.list.length) {
															checkRob.listIsShow = false;
															checkRob.listRemark = true;
														} else {
															checkRob.listIsShow = true;
															checkRob.listRemark = false;
															checkRob.remark = "";
														}
													});
								}

							}

							// 换页查询查退房明细表
							function getCheckOutDetailByLimits(p) {
								services.selectCheckOutDetailByLimits({
									limit : checkOutLimit,
									page : p
								}).success(function(data) {
									checkRob.CheckOutDetailList = data.list;
								});
							}
							// zq查退房效率分析
							checkRob.coaLimit = {
								checkYear : myDate.getFullYear(),
								quarter : '0',
								roomType : '-1',
								staffId : ''
							};
							checkRob.selectCheckOutAnalyseByLimits = function() {
								if (checkRob.coaLimit.checkYear == "") {
									alert("请填写查询年份！");
									return false;
								}
								if (checkRob.coaLimit.roomType == "") {
									alert("请选择房间类型！");
									return false;
								}
								if (checkRob.coaLimit.staffId == "") {
									alert("请选择查询员工！");
									return false;
								}
								$(".overlayer").fadeIn(200);
								$(".tipLoading").fadeIn(200);
								var checkOutAnalyseLimit = JSON
										.stringify(checkRob.coaLimit);
								services
										.selectCheckOutAnalyseByLimits({
											limit : checkOutAnalyseLimit
										})
										.success(
												function(data) {
													$(".overlayer")
															.fadeOut(200);
													$(".tipLoading").fadeOut(
															200);
													var title = "客房员工 "
															+ " "
															+ getSelectedRoomType(checkRob.reaLimit.roomType)
															+ " "
															+ "查退房用时分析折线图";// 折线图标题显示
													var xAxis = [];// 横坐标显示
													var yAxis = "查退房用时";// 纵坐标显示
													var nowQuarter = checkRob.coaLimit.quarter;// 当前的选择季度
													var lineName = getSelectedStaff(checkRob.coaLimit.staffId)
															+ "员工查退房用时";
													var lineData = [];// 最终传入chart1中的data
													var allAverageData = [];// 全体员工查退房效率的平均Data
													var averageData = [];// 个人平均查退房效率
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
																changeNumType(data.allAverWorkEfficiency),
																12);
														averageData = getAverageData(
																changeNumType(data.averWorkEfficiency),
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
														break;
													}
													combine(lineData,
															"个人平均查退房用时",
															averageData);
													combine(lineData,
															"全体平均查退房用时",
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
														checkRob.listRemark = true;
														checkRob.remark = data.analyseResult;
														$("#analyseResult")
																.val(
																		data.analyseResult);
													} else {
														checkRob.listRemark = false;
														checkRob.remark = "";
														$("#analyseResult")
																.val("");
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
								if ($location.path().indexOf(
										'/robEfficiencyForm') == 0) {
									selectRoomSorts();
									checkRob.sortName = "全部类型";

								} else if ($location.path().indexOf(
										'/robEffAnalyseForm') == 0) {
									selectRoomSorts();
									selectRoomStaffs(0);
								} else if ($location.path().indexOf(
										'/checkOutHomeForm') == 0) {
									selectRoomSorts();
								} else if ($location.path().indexOf(
										'/checkOutAnalyseForm') == 0) {
									selectRoomSorts();
									selectRoomStaffs(0);
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
// lwt:小数转换为百分数过滤器
app.filter('numPercent', function() {
	return function(input) {
		var number = (input * 100).toFixed(2) + "%";
		return number;
	}
});
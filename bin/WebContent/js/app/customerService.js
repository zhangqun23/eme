var app = angular
		.module(
				'customerService',
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
	$routeProvider.when('/depWorkloadForm', {
		templateUrl : '/HDR/jsp/customerService/depWorkloadForm.html',
		controller : 'CustomerServiceController'
	}).when('/staffWorkloadForm', {
		templateUrl : '/HDR/jsp/customerService/staffWorkloadForm.html',
		controller : 'CustomerServiceController'
	}).when('/typeForm', {
		templateUrl : '/HDR/jsp/customerService/typeForm.html',
		controller : 'CustomerServiceController'
	}).when('/expendForm', {
		templateUrl : '/HDR/jsp/customerService/expendForm.html',
		controller : 'CustomerServiceController'
	}).when('/expendAnalyse', {
		templateUrl : '/HDR/jsp/customerService/expendAnalyse.html',
		controller : 'CustomerServiceController'
	}).when('/staffExpend', {
		templateUrl : '/HDR/jsp/customerService/staffExpend.html',
		controller : 'CustomerServiceController'
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

							// lwt:查询部门对客服务工作量
							services.selectDepWorkload = function(data) {
								return $http({
									method : 'post',
									url : baseUrl
											+ 'customerServiceInformation/selectDepWorkload.do',
									data : data
								});
							};
							// lwt:查询员工对客服务工作量
							services.selectStaffWorkload = function(data) {
								return $http({
									method : 'post',
									url : baseUrl
											+ 'customerServiceInformation/selectStaffWorkload.do',
									data : data
								});
							};
							// lwt:查询客房部对客服务类型统计
							services.selectType = function(data) {
								return $http({
									method : 'post',
									url : baseUrl
											+ 'customerServiceInformation/selectType.do',
									data : data
								});
							};
							// lwt:查询部门列表
							services.selectDepart = function(data) {
								return $http({
									method : 'post',
									url : baseUrl
											+ 'customerServiceInformation/selectDep.do',
									data : data
								});
							};
							// lwt:根据部门ID筛选出员工列表
							services.selectStaffByDepId = function(data) {
								return $http({
									method : 'post',
									url : baseUrl
											+ 'customerServiceInformation/selectStaffByDepId.do',
									data : data
								});
							};
							// lwt:筛选出部门
							services.selectDep = function(data) {
								return $http({
									method : 'post',
									url : baseUrl + 'user/selectDep.do',
									data : data
								});
							};

							// wq选择布草消耗
							services.selectLinenExpendFormByLlimits = function(
									data) {
								return $http({
									method : 'post',
									url : baseUrl
											+ 'customerService/selectLinenExpendFormByLlimits.do',
									data : data
								});
							};
							// wq布草用量分析
							services.selectLinenExpendAnalyseByLlimits = function(
									data) {
								return $http({
									method : 'post',
									url : baseUrl
											+ 'customerService/selectLinenExpendAnalyseByLlimits.do',
									data : data
								});
							};
							// wq选择房间耗品
							services.selectRoomExpendFormByRlimits = function(
									data) {
								return $http({
									method : 'post',
									url : baseUrl
											+ 'customerService/selectRoomExpendFormByRlimits.do',
									data : data
								});
							};
							// wq房间耗品用量分析
							services.selectRoomExpendAnalyseByRlimits = function(
									data) {
								return $http({
									method : 'post',
									url : baseUrl
											+ 'customerService/selectRoomExpendAnalyseByRlimits.do',
									data : data
								});
							};
							// wq选择卫生间耗品
							services.selectWashExpendFormByWlimits = function(
									data) {
								return $http({
									method : 'post',
									url : baseUrl
											+ 'customerService/selectWashExpendFormByWlimits.do',
									data : data
								});
							};
							// wq卫生间耗品用量分析
							services.selectWashExpendAnalyseByWlimits = function(
									data) {
								return $http({
									method : 'post',
									url : baseUrl
											+ 'customerService/selectWashExpendAnalyseByWlimits.do',
									data : data
								});
							};
							// wq选择迷你吧耗品
							services.selectMiniExpendFormByMlimits = function(
									data) {
								return $http({
									method : 'post',
									url : baseUrl
											+ 'customerService/selectMiniExpendFormByMlimits.do',
									data : data
								});
							};
							// wq迷你吧用量分析
							services.selectStaExpendByLimits = function(data) {
								return $http({
									method : 'post',
									url : baseUrl
											+ 'customerService/selectStaExpendByLimits.do',
									data : data
								});
							};
							// wq员工领取耗品统计
							services.selectMiniExpendAnalyseByMlimits = function(
									data) {
								return $http({
									method : 'post',
									url : baseUrl
											+ 'customerService/selectMiniExpendAnalyseByMlimits.do',
									data : data
								});
							};
							return services;
						} ]);
app
		.controller(
				'CustomerServiceController',
				[
						'$scope',
						'services',
						'$location',
						function($scope, services, $location) {
							var reportForm = $scope;
							var nowPage = 1;
							var myDate = new Date();
							var myDateTime = new Date().format('yyyy-MM-dd');
							// wq布草分析统计界面设置条件
							reportForm.allimit = {
								startTime : myDateTime,
								endTime : myDateTime
							};
							// wq布草分析统计界面设置条件
							reportForm.arlimit = {
								startTime : myDateTime,
								endTime : myDateTime
							};
							// wq卫生间分析统计界面设置条件
							reportForm.awlimit = {
								startTime : myDateTime,
								endTime : myDateTime
							};
							// wq迷你吧分析统计界面设置条件
							reportForm.amlimit = {
								startTime : myDateTime,
								endTime : myDateTime
							};
							// wq耗品统计界面设置条件
							reportForm.reLimit = {
								tableType : "0",
								startTime : myDateTime,
								endTime : myDateTime,
								formType : "0"
							}
							// wq耗品分析界面设置条件
							reportForm.areLimit = {
								tableType : "0",
								startTime : myDateTime,
								endTime : myDateTime
							}
							// wq耗品分析界面设置条件
							reportForm.sLimit = {
								tableType : "0",
								startTime : myDateTime,
								endTime : myDateTime
							}
							// wq获取报表类型名称
							reportForm.formName = "对客服务";
							// wq选择报表类型默认值
							reportForm.formTypes = [ {
								id : 0,
								type : "对客服务"
							}, {
								id : 1,
								type : "离退房"
							}, {
								id : 2,
								type : "过夜房"
							} ];

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
							// wq布草统计换页函数
							function getLinenExpendFormByLlimits(p) {
								$(".overlayer").fadeIn(200);
								$(".tipLoading").fadeIn(200);
								services.selectLinenExpendFormByLlimits({
									llimit : expendFormLimit,// linenExpendFormLlimit,
									page : p
								}).success(function(data) {
									$(".overlayer").fadeOut(200);
									$(".tipLoading").fadeOut(200);
									reportForm.linenExpendFormList = data.list;
								});
							}
							// wq房间耗品统计换页函数
							function getRoomExpendFormByRlimits(p) {
								$(".overlayer").fadeIn(200);
								$(".tipLoading").fadeIn(200);
								services.selectRoomExpendFormByRlimits({
									rlimit : expendFormLimit,// roomExpendFormRlimit,
									page : p
								}).success(function(data) {
									$(".overlayer").fadeOut(200);
									$(".tipLoading").fadeOut(200);
									reportForm.roomExpendFormList = data.list;
								});
							}
							// wq卫生间耗品统计换页函数
							function getWashExpendFormByWlimits(p) {
								$(".overlayer").fadeIn(200);
								$(".tipLoading").fadeIn(200);
								services.selectWashExpendFormByWlimits({
									wlimit : expendFormLimit,
									page : p
								}).success(function(data) {
									$(".overlayer").fadeOut(200);
									$(".tipLoading").fadeOut(200);
									reportForm.washExpendFormList = data.list;
								});
							}
							// wq迷你吧统计换页函数
							function getMiniExpendFormByMlimits(p) {
								$(".overlayer").fadeIn(200);
								$(".tipLoading").fadeIn(200);
								services.selectMiniExpendFormByMlimits({
									mlimit : expendFormLimit,
									page : p
								}).success(function(data) {
									$(".overlayer").fadeOut(200);
									$(".tipLoading").fadeOut(200);
									reportForm.miniExpendFormList = data.list;
								});
							}
							// wq查询员工领取耗品用量统计
							reportForm.selectStaExpendByLimits = function() {
								if (reportForm.sLimit.startTime == "") {
									alert("请选择开始时间！");
									return false;
								}
								if (reportForm.sLimit.endTime == "") {
									alert("请选择截止时间！");
									return false;
								}
								if (compareDateTime(
										reportForm.sLimit.startTime,
										reportForm.sLimit.endTime)) {
									alert("截止时间不能大于开始时间！");
									return false;
								}
								$(".overlayer").fadeIn(200);
								$(".tipLoading").fadeIn(200);
								expendFormLimit = JSON
										.stringify(reportForm.sLimit);
								services
										.selectStaExpendByLimits({
											sLimit : expendFormLimit
										})
										.success(
												function(data) {
													$(".overlayer")
															.fadeOut(200);
													$(".tipLoading").fadeOut(
															200);
													reportForm.expendFormList = data.list;
													reportForm.expendCount = data.count;
													reportForm.analyseResult = data.analyseResult;
													if (data.list) {
														reportForm.listIsShow = false;
													} else {
														reportForm.listIsShow = true;
													}
													if (reportForm.analyseResult) {
														switch (reportForm.sLimit.tableType) {
														case '0':
															reportForm.listRemark1 = true;
															reportForm.listRemark2 = false;
															reportForm.listRemark3 = false;
															reportForm.listRemark4 = false;
															break;
														case '1':
															reportForm.listRemark1 = false;
															reportForm.listRemark2 = true;
															reportForm.listRemark3 = false;
															reportForm.listRemark4 = false;
															break;
														case '2':
															reportForm.listRemark1 = false;
															reportForm.listRemark2 = false;
															reportForm.listRemark3 = true;
															reportForm.listRemark4 = false;
															break;
														case '3':
															reportForm.listRemark1 = false;
															reportForm.listRemark2 = false;
															reportForm.listRemark3 = false;
															reportForm.listRemark4 = true;
															break;
														}
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
							// wq查询耗品用量统计
							reportForm.selectFormByLimits = function() {
								if (reportForm.reLimit.startTime == "") {
									alert("请选择开始时间！");
									return false;
								}
								if (reportForm.reLimit.endTime == "") {
									alert("请选择截止时间！");
									return false;
								}
								if (compareDateTime(
										reportForm.reLimit.startTime,
										reportForm.reLimit.endTime)) {
									alert("截止时间不能大于开始时间！");
									return false;
								}
								$(".overlayer").fadeIn(200);
								$(".tipLoading").fadeIn(200);
								expendFormLimit = JSON
										.stringify(reportForm.reLimit);
								switch (reportForm.reLimit.tableType) {
								case '0':
									services
											.selectLinenExpendFormByLlimits({
												llimit : expendFormLimit,
												page : 1
											})
											.success(
													function(data) {
														$(".overlayer")
																.fadeOut(200);
														$(".tipLoading")
																.fadeOut(200);
														reportForm.linenExpendFormList = data.list;
														reportForm.totalPage = data.totalPage;
														reportForm.analyseResult = data.analyseResult;
														pageTurn(
																reportForm.totalPage,
																1,
																getLinenExpendFormByLlimits);
														reportForm.pageShow = true;
														reportForm.linenCount = data.linenCount;
														if (reportForm.analyseResult) {
															reportForm.remark = data.analyseResult;
															reportForm.listRemark1 = true;
															reportForm.listRemark2 = false;
															reportForm.listRemark3 = false;
															reportForm.listRemark4 = false;
															$("#analyseResult")
																	.val(
																			data.analyseResult);
														} else {
															reportForm.listRemark = false;
															reportForm.remark = "";
															$("#analyseResult")
																	.val("");
														}
														if (data.list.length) {
															reportForm.listIsShow = false;
														} else {
															reportForm.listIsShow = true;
														}
													});
									break;
								case '1':
									services
											.selectRoomExpendFormByRlimits({
												rlimit : expendFormLimit,
												page : 1
											})
											.success(
													function(data) {
														$(".overlayer")
																.fadeOut(200);
														$(".tipLoading")
																.fadeOut(200);
														reportForm.roomExpendFormList = data.list;
														pageTurn(
																data.totalPage,
																1,
																getRoomExpendFormByRlimits);
														reportForm.pageShow = true;
														reportForm.roomCount = data.roomCount;
														reportForm.analyseResult = data.analyseResult;
														if (reportForm.analyseResult) {
															reportForm.remark = data.analyseResult;
															reportForm.listRemark1 = false;
															reportForm.listRemark2 = true;
															reportForm.listRemark3 = false;
															reportForm.listRemark4 = false;
															$("#analyseResult")
																	.val(
																			data.analyseResult);
														} else {
															reportForm.listRemark = false;
															reportForm.remark = "";
															$("#analyseResult")
																	.val("");
														}
														if (data.list.length) {
															reportForm.listIsShow = false;
														} else {
															reportForm.listIsShow = true;
														}
													});
									break;
								case '2':
									services
											.selectWashExpendFormByWlimits({
												wlimit : expendFormLimit,
												page : 1
											})
											.success(
													function(data) {
														$(".overlayer")
																.fadeOut(200);
														$(".tipLoading")
																.fadeOut(200);
														reportForm.washExpendFormList = data.list;
														reportForm.totalPage = data.totalPage;
														pageTurn(
																reportForm.totalPage,
																1,
																getWashExpendFormByWlimits);
														reportForm.pageShow = true;
														reportForm.washCount = data.washCount;
														reportForm.analyseResult = data.analyseResult;
														if (reportForm.analyseResult) {
															reportForm.remark = data.analyseResult;
															reportForm.listRemark1 = false;
															reportForm.listRemark2 = false;
															reportForm.listRemark3 = true;
															reportForm.listRemark4 = false;
															$("#analyseResult")
																	.val(
																			data.analyseResult);
														} else {
															reportForm.listRemark = false;
															reportForm.remark = "";
															$("#analyseResult")
																	.val("");
														}
														if (data.list.length) {
															reportForm.listIsShow = false;
														} else {
															reportForm.listIsShow = true;
														}
													});
									break;
								case '3':
									services
											.selectMiniExpendFormByMlimits({
												mlimit : expendFormLimit,
												page : 1
											})
											.success(
													function(data) {
														$(".overlayer")
																.fadeOut(200);
														$(".tipLoading")
																.fadeOut(200);
														reportForm.miniExpendFormList = data.list;
														reportForm.totalPage = data.totalPage;
														pageTurn(
																reportForm.totalPage,
																1,
																getMiniExpendFormByMlimits);
														reportForm.pageShow = true;
														reportForm.miniCount = data.miniCount;
														reportForm.analyseResult = data.analyseResult;
														if (reportForm.analyseResult) {
															reportForm.remark = data.analyseResult;
															reportForm.listRemark1 = false;
															reportForm.listRemark2 = false;
															reportForm.listRemark3 = false;
															reportForm.listRemark4 = true;
															$("#analyseResult")
																	.val(
																			data.analyseResult);
														} else {
															reportForm.listRemark = false;
															reportForm.remark = "";
															$("#analyseResult")
																	.val("");
														}
														if (data.list.length) {
															reportForm.listIsShow = false;
														} else {
															reportForm.listIsShow = true;
														}
													});
									break;
								}
							}
							reportForm.linenTable = true;
							reportForm.roomTable = false;
							reportForm.washTable = false;
							reportForm.miniTable = false;
							// wq根据选择的耗品类型显示不同的报表
							reportForm.changeTable = function() {
								reportForm.pageShow = false;
								reportForm.expendFormList = "";
								reportForm.expendCount = "";
								reportForm.listRemark1 = false;
								reportForm.listRemark2 = false;
								reportForm.listRemark3 = false;
								reportForm.listRemark4 = false;
								var table = $("#tableType").val();
								switch (table) {
								case '0':
									reportForm.linenTable = true;
									reportForm.roomTable = false;
									reportForm.washTable = false;
									reportForm.miniTable = false;
									break;
								case '1':
									reportForm.linenTable = false;
									reportForm.roomTable = true;
									reportForm.washTable = false;
									reportForm.miniTable = false;
									break;
								case '2':
									reportForm.linenTable = false;
									reportForm.roomTable = false;
									reportForm.washTable = true;
									reportForm.miniTable = false;
									break;
								case '3':
									reportForm.linenTable = false;
									reportForm.roomTable = false;
									reportForm.washTable = false;
									reportForm.miniTable = true;
									break;
								}
							}
							// wq查询耗品用量分析
							reportForm.selectAnalyseByLimits = function() {
								if (reportForm.areLimit.startTime == "") {
									alert("请选择开始时间！");
									return false;
								}
								if (reportForm.areLimit.endTime == "") {
									alert("请选择截止时间！");
									return false;
								}
								if (compareDateTime(
										reportForm.areLimit.startTime,
										reportForm.areLimit.endTime)) {
									alert("截止时间不能大于开始时间！");
									return false;
								}
								$(".overlayer").fadeIn(200);
								$(".tipLoading").fadeIn(200);
								expendAnalyseLimit = JSON
										.stringify(reportForm.areLimit);
								switch (reportForm.areLimit.tableType) {
								case '0':
									services
											.selectLinenExpendAnalyseByLlimits(
													{
														allimit : expendAnalyseLimit
													})
											.success(
													function(data) {
														$(".overlayer")
																.fadeOut(200);
														$(".tipLoading")
																.fadeOut(200);
														$('#linenbar-svg')
																.empty();
														$('#linenpie-svg')
																.empty();
														$('#linenpieChart')
																.empty();
														$('#linenbar1').empty();
														reportForm.remark1 = "";
														if (data.list) {

															reportForm.listIsShow = false;
															if (data.list.length < 15) {
																reportForm.barSize = data.list.length * 80;
															} else {
																reportForm.barSize = 1200;
															}
															$("#bar1")
																	.css(
																			'height',
																			reportForm.barSize
																					+ 'px');
															var title = "客房部布草使用量条形图分析";// 条形图标题显示
															var xAxis = [];// 横坐标显示
															var yAxis = "单位:数量";// 纵坐标显示
															var barData = [];// 最终传入bar1中的data
															var linenNum = [];
															var pieData = [];// 饼状图传入数据
															for ( var item in data.list) {
																if (data.list[item].good_name != '') {
																	xAxis
																			.push(data.list[item].goods_name);
																	linenNum
																			.push(parseInt(data.list[item].goods_num));
																	combinePie(
																			pieData,
																			data.list[item].goods_name,
																			parseInt(data.list[item].goods_num));
																}
															}
															pieChartForm(
																	"#linenpieChart",
																	"客房部布草使用量饼状图分析",
																	"布草使用占比",
																	pieData);
															$('#linenpie-svg')
																	.val(
																			$(
																					"#linenpieChart")
																					.highcharts()
																					.getSVG());

															combine(barData,
																	'布草使用数量',
																	linenNum);
															barForm(
																	barData,
																	"#linenbar1",
																	title,
																	xAxis,
																	yAxis);
															$('#linenbar-svg')
																	.val(
																			$(
																					"#linenbar1")
																					.highcharts()
																					.getSVG());
															$('#roombar-svg')
																	.val("");
															$('#washbar-svg')
																	.val("");
															$('#minibar-svg')
																	.val("");
															$('#minipieChart')
																	.val("");

															reportForm.remark1 = '';
															reportForm.remark2 = '';
															reportForm.remark3 = '';
															reportForm.remark4 = '';
															if (data.analyseResult) {
																reportForm.listRemark = true;
																reportForm.remark1 = data.analyseResult;
																$(
																		"#analyseResult")
																		.val(
																				data.analyseResult);
																reportForm.remark2 = '';
																reportForm.remark3 = '';
																reportForm.remark4 = '';
															} else {
																reportForm.listRemark = false;
																reportForm.remark1 = "";
																$(
																		"#analyseResult")
																		.val("");
															}
														} else {
															reportForm.listIsShow = true;
														}
													});
									break;
								case '1':
									services
											.selectRoomExpendAnalyseByRlimits({
												arlimit : expendAnalyseLimit
											})
											.success(
													function(data) {
														$(".overlayer")
																.fadeOut(200);
														$(".tipLoading")
																.fadeOut(200);
														$('#roombar-svg')
																.empty();
														$('#roombar1').empty();
														reportForm.remark2 = "";
														if (data.list) {
															reportForm.listIsShow = false;

															if (data.list.length < 15) {
																reportForm.barSize = data.list.length * 80;
															} else {
																reportForm.barSize = 1200;
															}
															$("#bar1")
																	.css(
																			'height',
																			reportForm.barSize
																					+ 'px');
															var title = "客房部房间耗品使用量条形图分析";// 条形图标题显示
															var xAxis = [];// 横坐标显示
															var yAxis = "单位:数量";// 纵坐标显示
															var barData = [];// 最终传入bar1中的data
															var linenNum = [];
															for ( var item in data.list) {
																if (data.list[item].good_name != '') {
																	xAxis
																			.push(data.list[item].goods_name);
																	linenNum
																			.push(parseInt(data.list[item].goods_num));
																}
															}
															combine(barData,
																	'房间耗品使用数量',
																	linenNum);
															barForm(
																	barData,
																	"#roombar1",
																	title,
																	xAxis,
																	yAxis);
															$('#roombar-svg')
																	.val(
																			$(
																					"#roombar1")
																					.highcharts()
																					.getSVG());
															$('#linenbar-svg')
																	.val("");
															$('#linenpieChart')
																	.val("");
															$('#washbar-svg')
																	.val("");
															$('#minibar-svg')
																	.val("");
															$('#minipieChart')
																	.val("");
															reportForm.remark1 = '';
															reportForm.remark2 = '';
															reportForm.remark3 = '';
															reportForm.remark4 = '123455';
															if (data.analyseResult) {
																reportForm.listRemark = true;
																reportForm.remark2 = data.analyseResult;
																$(
																		"#analyseResult")
																		.val(
																				data.analyseResult);
																reportForm.remark1 = '';
																reportForm.remark3 = '';
																reportForm.remark4 = '';
															} else {
																reportForm.listRemark = false;
																reportForm.remark2 = "";
																$(
																		"#analyseResult")
																		.val("");
															}
														} else {
															reportForm.listIsShow = true;
														}

													});
									break;
								case '2':
									services
											.selectWashExpendAnalyseByWlimits({
												wrlimit : expendAnalyseLimit
											})
											.success(
													function(data) {
														$(".overlayer")
																.fadeOut(200);
														$(".tipLoading")
																.fadeOut(200);
														$('#washbar-svg')
																.empty();
														;
														$('#washbar').empty();
														reportForm.remark3 = "";
														if (data.list) {
															reportForm.listIsShow = false;

															if (data.list.length < 15) {
																reportForm.barSize = data.list.length * 80;
															} else {
																reportForm.barSize = 1200;
															}
															$("#bar1")
																	.css(
																			'height',
																			reportForm.barSize
																					+ 'px');
															var title = "客房部卫生间耗品使用量条形图分析";// 条形图标题显示
															var xAxis = [];// 横坐标显示
															var yAxis = "单位:数量";// 纵坐标显示
															var barData = [];// 最终传入bar1中的data
															var linenNum = [];
															for ( var item in data.list) {
																if (data.list[item].goods_name != '') {
																	xAxis
																			.push(data.list[item].goods_name);
																	linenNum
																			.push(parseInt(data.list[item].goods_num));
																}
															}
															combine(
																	barData,
																	'卫生间耗品使用数量',
																	linenNum);
															barForm(
																	barData,
																	"#washbar1",
																	title,
																	xAxis,
																	yAxis);
															$('#washbar-svg')
																	.val(
																			$(
																					"#washbar1")
																					.highcharts()
																					.getSVG());
															$('#linenbar-svg')
																	.val("");
															$('#linenpieChart')
																	.val("");
															$('#roombar-svg')
																	.val("");
															$('#minibar-svg')
																	.val("");
															$('#minipieChart')
																	.val("");
															reportForm.remark1 = '';
															reportForm.remark2 = '';
															reportForm.remark3 = '';
															reportForm.remark4 = '';
															if (data.analyseResult) {
																reportForm.listRemark = true;
																reportForm.remark3 = data.analyseResult;
																$(
																		"#analyseResult")
																		.val(
																				data.analyseResult);
																reportForm.remark1 = '';
																reportForm.remark2 = '';
																reportForm.remark4 = '';
															} else {
																reportForm.listRemark = false;
																reportForm.remark3 = "";
																$(
																		"#analyseResult")
																		.val("");
															}
														} else {
															reportForm.listIsShow = true;
														}

													});
									break;
								case '3':
									services
											.selectMiniExpendAnalyseByMlimits({
												amlimit : expendAnalyseLimit
											})
											.success(
													function(data) {
														$(".overlayer")
																.fadeOut(200);
														$(".tipLoading")
																.fadeOut(200);
														reportForm.typeList = data.list;
														$('#minibar-svg')
																.empty();
														$('#minipie-svg')
																.empty();
														$('#minipieChart')
																.empty();
														$('#minibar1').empty();
														reportForm.remark4 = "";
														if (data.list) {
															reportForm.listIsShow = false;

															if (data.list.length < 15) {
																reportForm.barSize = data.list.length * 80;
															} else {
																reportForm.barSize = 1200;
															}
															$("#bar1")
																	.css(
																			'height',
																			reportForm.barSize
																					+ 'px');
															var title = "客房部迷你吧使用量条形图分析";// 条形图标题显示
															var xAxis = [];// 横坐标显示
															var yAxis = "单位:数量";// 纵坐标显示
															var barData = [];// 最终传入bar1中的data
															var miniNum = [];
															var pieData = [];// 饼状图传入数据
															for ( var item in data.list) {
																if (data.list[item].good_name != '') {
																	xAxis
																			.push(data.list[item].goods_name);
																	miniNum
																			.push(parseInt(data.list[item].goods_num));
																	combinePie(
																			pieData,
																			data.list[item].goods_name,
																			parseInt(data.list[item].goods_num));
																}
															}
															pieChartForm(
																	"#minipieChart",
																	"客房部迷你吧使用量饼状图分析",
																	"迷你吧使用占比",
																	pieData);
															$('#minipie-svg')
																	.val(
																			$(
																					"#minipieChart")
																					.highcharts()
																					.getSVG());

															combine(barData,
																	'迷你吧使用数量',
																	miniNum);
															barForm(
																	barData,
																	"#minibar1",
																	title,
																	xAxis,
																	yAxis);
															$('#minibar-svg')
																	.val(
																			$(
																					"#minibar1")
																					.highcharts()
																					.getSVG());
															$('#linenbar-svg')
																	.val("");
															$('#linenpieChart')
																	.val("");
															$('#roombar-svg')
																	.val("");
															$('#washbar-svg')
																	.val("");
															reportForm.remark1 = '';
															reportForm.remark2 = '';
															reportForm.remark3 = '';
															reportForm.remark4 = '';
															if (data.analyseResult) {
																reportForm.listRemark = true;
																reportForm.remark4 = data.analyseResult;
																$(
																		"#analyseResult")
																		.val(
																				data.analyseResult);
															} else {
																reportForm.listRemark = false;
																reportForm.remark4 = "";
																$(
																		"#analyseResult")
																		.val("");
																reportForm.remark2 = '';
																reportForm.remark3 = '';
																reportForm.remark1 = '';
															}
														} else {
															reportForm.listIsShow = true;
														}

													});
									break;
								}
							}
							reportForm.linenPic = true;
							reportForm.roomPic = false;
							reportForm.washPic = false;
							reportForm.miniPic = false;
							// wq根据选择的耗品类型显示不同的图
							reportForm.changePic = function() {
								var table = $("#tableType").val();
								reportForm.listRemark = '';
								switch (table) {
								case '0':
									reportForm.linenPic = true;
									reportForm.roomPic = false;
									reportForm.washPic = false;
									reportForm.miniPic = false;
									break;
								case '1':
									reportForm.linenPic = false;
									reportForm.roomPic = true;
									reportForm.washPic = false;
									reportForm.miniPic = false;
									break;
								case '2':
									reportForm.linenPic = false;
									reportForm.roomPic = false;
									reportForm.washPic = true;
									reportForm.miniPic = false;
									break;
								case '3':
									reportForm.linenPic = false;
									reportForm.roomPic = false;
									reportForm.washPic = false;
									reportForm.miniPic = true;
									break;
								}
							}
							// wq传入报表名称
							reportForm.getFormNameByNo = function() {
								var no = $("#linenFormType").val();
								reportForm.formName = getSelectedFormName(no);
							}
							// wq获取所选报表名称
							function getSelectedFormName(formType) {
								var type = "";
								switch (formType) {
								case '0':
									type = "对客服务";
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
							// wq比较两个时间的大小
							function compareDateTime(startDate, endDate) {
								var date1 = new Date(startDate);
								var date2 = new Date(endDate);
								if (date2.getTime() < date1.getTime()) {
									return true;
								} else {
									return false;
								}
							}
							// wq换页
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
							// wq扇形图图公用函数
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
							// lwt对客服务部门设置条件
							reportForm.depWorkloadLimit = {
								start_time : myDateTime,
								end_time : myDateTime
							};
							// lwt对客服务员工工作量统计设置条件
							reportForm.staffWorkloadLimit = {
								start_time : myDateTime,
								end_time : myDateTime,
								depart : ""
							}
							// lwt对客服务类型统计
							reportForm.typeLimit = {
								start_time : myDateTime,
								end_time : myDateTime,
								depart : ""

							}

							// lwt查询部门列表
							function selectDepart() {
								services.selectDepart().success(function(data) {
									reportForm.depts = data;
								});
							}
							// lwt根据部门id筛选出员工列表
							function selectStaffByDepId() {
								services.selectStaffByDepId().success(
										function(data) {
											reportForm.staffs = data.list;
										});
							}
							// lwt部门名称
							reportForm.deptName = "";
							// lwt当部门下拉框变化时获取部门名字
							reportForm.getDeptNameById = function() {
								var Id = $("#dept").val();
								reportForm.deptName = getDeptName(Id);
							}
							// lwt根据deptId获取部门名称
							function getDeptName(deptId) {
								var type = "";
								for ( var item in reportForm.depts) {
									if (reportForm.depts[item].departmentId == deptId) {
										type = reportForm.depts[item].departmentName;
									}
								}
								return type;
							}

							// lwt根据条件查找部门对客服务工作量
							reportForm.selectDepWorkload = function() {
								if (reportForm.depWorkloadLimit.start_time == "") {
									alert("请选择开始时间！");
									return false;
								}
								if (reportForm.depWorkloadLimit.end_time == "") {
									alert("请选择截止时间！");
									return false;
								}
								if (compareDateTime(
										reportForm.depWorkloadLimit.start_time,
										reportForm.depWorkloadLimit.end_time)) {
									alert("截止时间不能大于开始时间！");
									return false;
								}
								$(".overlayer").fadeIn(200);
								$(".tipLoading").fadeIn(200);
								var depWorkloadLimit = JSON
										.stringify(reportForm.depWorkloadLimit);
								services.selectDepWorkload({
									limit : depWorkloadLimit
								}).success(function(data) {
									$(".overlayer").fadeOut(200);
									$(".tipLoading").fadeOut(200);
									reportForm.depWorkloadList = data.list;
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
							// lwt根据条件查找员工对客服务工作量
							reportForm.selectStaffWorkload = function() {
								if (reportForm.staffWorkloadLimit.start_time == "") {
									alert("请选择开始时间！");
									return false;
								}
								if (reportForm.staffWorkloadLimit.end_time == "") {
									alert("请选择截止时间！");
									return false;
								}
								if (compareDateTime(
										reportForm.staffWorkloadLimit.start_time,
										reportForm.staffWorkloadLimit.end_time)) {
									alert("截止时间不能大于开始时间！");
									return false;
								}
								if (reportForm.staffWorkloadLimit.depart == "") {
									alert("请选择部门！");
									return false;
								}
								$(".overlayer").fadeIn(200);
								$(".tipLoading").fadeIn(200);

								var staffWorkloadLimit = JSON
										.stringify(reportForm.staffWorkloadLimit);
								services.selectStaffWorkload({
									limit : staffWorkloadLimit
								}).success(function(data) {
									$(".overlayer").fadeOut(200);
									$(".tipLoading").fadeOut(200);
									reportForm.staffWorkloadList = data.list;
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
							reportForm.barSize = "";
							// lwt根据条件查找服务类型统计
							reportForm.selectType = function() {
								if (reportForm.typeLimit.start_time == "") {
									alert("请选择开始时间！");
									return false;
								}
								if (reportForm.typeLimit.end_time == "") {
									alert("请选择截止时间！");
									return false;
								}
								if (compareDateTime(
										reportForm.typeLimit.start_time,
										reportForm.typeLimit.end_time)) {
									alert("截止时间不能大于开始时间！");
									return false;
								}
								if (reportForm.typeLimit.depart == "") {
									alert("请选择部门！");
									return false;
								}
								$(".overlayer").fadeIn(200);
								$(".tipLoading").fadeIn(200);
								var typeLimit = JSON
										.stringify(reportForm.typeLimit);
								services
										.selectType({
											limit : typeLimit
										})
										.success(
												function(data) {
													$(".overlayer")
															.fadeOut(200);
													$(".tipLoading").fadeOut(
															200);

													if (data.list.length == 1) {
														reportForm.typeList = '';
														reportForm.listIsShow = true;
														reportForm.listRemark = false;
														reportForm.barIsShow = false;
													} else {
														reportForm.barIsShow = true;
														reportForm.typeList = data.list;
														reportForm.listIsShow = false;
														reportForm.listRemark = true;
														reportForm.remark = "";
														if (data.list.length < 15) {
															reportForm.barSize = data.list.length * 80;
														} else {
															reportForm.barSize = 1200;
														}
														$("#bar1")
																.css(
																		'height',
																		reportForm.barSize
																				+ 'px');
														var title = getSelectedDepartName(reportForm.typeLimit.depart)
																+ "对客服务类型条形图分析";// 条形图标题显示
														var xAxis = [];// 横坐标显示
														var yAxis = "单位:数量";// 纵坐标显示
														var barData = [];// 最终传入bar1中的data
														var serviceLoads = [];
														for ( var item in data.list) {
															if (data.list[item].serviceType != '') {
																xAxis
																		.push(data.list[item].serviceType);
																serviceLoads
																		.push(parseInt(data.list[item].serviceLoad));
															}
														}
														combine(barData,
																'服务完成数量',
																serviceLoads);
														barForm(barData,
																"#bar1", title,
																xAxis, yAxis);
														$('#bar-svg')
																.val(
																		$(
																				"#bar1")
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
													}

												});
							}
							// lwt为生成饼图拼data
							function combinePie(da, name, data1) {
								var ss = [];
								ss[0] = name;
								ss[1] = data1;
								da.push(ss);
							}
							// lwt为生成条形图拼data
							function combine(da, name, arr) {
								var ss = new Object();
								ss.name = name;
								ss.data = arr;
								da.push(ss);
							}
							// lwt条形图公用函数
							function barForm(data, elementId, title, x_Axis,
									y_title) {
								var bar1 = new BarChart({
									elementId : elementId,
									title : title,
									subTitle : '',
									hx_Axis : x_Axis,
									hy_title : y_title,
									unit : '',
									data : data
								});
								bar1.init();
							}
							// lwt获取所选部门的名称
							function getSelectedDepartName(departId) {
								var departName = "";
								for ( var item in reportForm.depts) {
									if (reportForm.depts[item].departmentId == departId) {
										departName = reportForm.depts[item].departmentName;
									}
								}
								return departName;
							}
							// 初始化
							function initData() {
								console.log("初始化页面信息");

								if ($location.path().indexOf(
										'/staffWorkloadForm') == 0) {
									selectDepart();
									selectStaffByDepId();

								} else if ($location.path()
										.indexOf('/typeForm') == 0) {
									selectDepart();
								}
							}
							initData();
						} ]);
// lwt:小数转换为百分数过滤器
app.filter('numPercent', function() {
	return function(input) {
		var number = (input * 100).toFixed(2) + "%";
		return number;
	}
});

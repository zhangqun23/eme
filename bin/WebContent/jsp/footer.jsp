</section>
<!-- <footer> </footer> -->
<script type="text/javascript" src="${ctx}/js/lib/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="${ctx}/js/lib/pageTurn.js"></script>
<script type="text/javascript"
	src="${ctx}/js/lib/jquery.json-2.2.min.js"></script>
<!-- AngularJS dependences -->
<script type="text/javascript" src="${ctx}/js/lib/angular/angular.js"></script>
<script type="text/javascript" src="${ctx}/js/app/exportForm.js"></script>
<script type="text/javascript"
	src="${ctx}/js/lib/angular/angular-route.js"></script>
<script src="https://code.highcharts.com/highcharts.js"
	type="text/javascript" charset="utf-8"></script>
<script src="https://code.highcharts.com/modules/exporting.js"
	type="text/javascript" charset="utf-8"></script>
<script type="text/javascript">
	$(function() {
		//é¡¶é¨å¯¼èªåæ¢
		$(".nav li a").click(function() {
			$(".nav li a.selected").removeClass("selected");
			$(this).addClass("selected");
		});
	});

	$(function() {
		//å¯¼èªåæ¢
		$(".menuson li").click(function() {
			$(".menuson li.active").removeClass("active");
			$(this).addClass("active");
		});

		$('.title').click(function() {
			var $ul = $(this).next('ul');
			$('dd').find('ul').slideUp();
			if ($ul.is(':visible')) {
				$(this).next('ul').slideUp();
			} else {
				$(this).next('ul').slideDown();
			}
		});
	});
</script>

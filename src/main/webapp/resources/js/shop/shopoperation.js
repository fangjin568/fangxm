/**
 * 
 */
$(function(){
	var initUrl = 'sc/shopadmin/getshopinitinfo';
	var registerShopUrl = 'sc/shopadmin/registershop';
	function getShopInitInfo(){
		$.getJSON(initUrl,function(data)){
			if(data.success){
				var tempHtml = '';
				var tempAreaHtml = '';
				data.shopCategoryList.map(function(item,index){
					tempHtml +='<option data-id="'+item.shopCategoryId+'">'+item.shopCategoryName+'</option>';
				});
				data.areaList.map(function(item,index){
					tempAreaHtml += '<option data-id="'+
				})
			}
		}
	}
	
})
$package('Admin.main');

Admin.main = function(){
	return {
		treeSelect:function(){
			var _this = $(this);
			var title=_this.text();
			var url=_this.attr('href');
			Admin.main.addTab(title,url);
			return false;
		},
		treeInit:function(){
			var  $items =  $('#tree-box').find(".menu-item");
			$items.bind('click',this.treeSelect);
		},
		addTab:function(_title,_url){
			var boxId = '#tab-box';
			if($(boxId).tabs('exists',_title) ){
				var tab = $(boxId).tabs('getTab',_title);
				var index = $(boxId).tabs('getTabIndex',tab);
				$(boxId).tabs('select',index);
				if(tab && tab.find('iframe').length > 0){  
					 var _refresh_ifram = tab.find('iframe')[0];
				     _refresh_ifram.contentWindow.location.href=_url;  
			    } 
			}else{		
				var _content ="<iframe scrolling='auto' frameborder='0' src='"+_url+"' style='width:100%; height:100%'></iframe>";
				$(boxId).tabs('add',{
					    title:_title,
					    content:_content,
					    closable:true});
				
			}
		},
		menuHover:function(){
			//菜单鼠标进入效果
			$('.menu-item').hover(
				function () {
					$(this).stop().animate({ paddingLeft: "25px" }, 200,function(){
						$(this).addClass("orange");
					});
				}, 
				function () {
					$(this).stop().animate({ paddingLeft: "15px" },function(){
						$(this).removeClass("orange");
					});
				}
			);
		},
		init:function(){
			this.treeInit();
			this.menuHover();
		}
	};
}();

/* �Զ���������֤*/
$.extend($.fn.validatebox.defaults.rules, {  
    equals: {  
        validator: function(value,param){  
            return value == $(param[0]).val();  
        },  
        message: 'newpassword do not match.'  
    }  
});

$(function(){
	Admin.main.init();
});
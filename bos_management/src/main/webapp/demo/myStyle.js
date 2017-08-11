$(function(){
	var setting = {
				data: {
					simpleData: {
						enable: true
					}
				},
				callback: {
					onClick: function(event, treeId, treeNode, clickFlag) {
						var content = '<div style="width:100%;height:100%;overflow:scroll;"><iframe src='
						+treeNode.page+' scrolling="no" style="width:100%;height:100%;border:1;"></iframe></div>'
						
						// 没有就不打开
						if(treeNode.page != undefined && treeNode.page != ""){
							
							if($("#tab1").tabs('exists',treeNode.name)){
								$("#tab1").tabs('select',treeNode.name)
							}else{
								$("#tab1").tabs('add', {
								title: treeNode.name,
								content: content,
								closable: true
							})
							}
						}
					}
				}
			};

			var zNodes = [{
					id: 1,
					pId: 0,
					name: "父节点1",
					open: true
				},
				{
					id: 11,
					pId: 1,
					name: "子节点11",
					page: "http://www.baidu.com"
				},
				{
					id: 12,
					pId: 1,
					name: "子节点12",
					page: "demo3.html"
				},
				{
					id: 2,
					pId: 0,
					name: "父节点2"
				},
				{
					id: 21,
					pId: 2,
					name: "子节点21"
				},
				{
					id: 22,
					pId: 2,
					name: "子节点22"
				}
			];

			
			$(function(){
				
				$.fn.zTree.init($("#treeDemo"), setting, zNodes);
				
				$("#tab1").tabs({
					onContextMenu:function(e, title,index){
						e.preventDefault();
						$("#md").menu('show',{
							left:e.pageX,
							top:e.pageY
						})
					}
				})
				
				$("#md").menu({
					onClick:function(item){
						
						var tab = $("#tab1").tabs('getSelected');
						var index = $("#tab1").tabs('getTabIndex',tab);
						var length = $("#tab1").tabs('tabs').length;
						if("closeSelf" == item.id){
							$("#tab1").tabs('close',index);
						}
						if("closeAll" == item.id){
							for(var i = 0; i<length ;i++ ){
								$("#tab1").tabs('close',0);
							}
						}
						if("closeOther" == item.id){
							for(var i = 0; i < $("#tab1").tabs('tabs').length;i++ ){
								if(i != $("#tab1").tabs('getTabIndex',tab) ){
									$("#tab1").tabs('close',i);
									i = i-1;
								}
							}
						}
						if("closeLeft" == item.id){
							for(var i = 0; i < index;i++ ){
								$("#tab1").tabs('close',0);
							}
						}	
						
						if("closeRight" == item.id){
							for(var i = length; i > index;i-- ){
								$("#tab1").tabs('close',i);
							}
						}
					}
				})
			})
			
			
})
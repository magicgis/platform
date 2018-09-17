	$(function(){
		var oInput = $('#race');
		var oUl = $('#list');
		var oLi = oUl.find('li');
		oInput.on('focus',function(){
			oUl.show();
			oLi.on('click',function(){
				oInput.val($(this).html());
				oUl.hide();	
			});	
			 	
			$(this).on('input',function(){
			
				var inputVal = $(this).val();
				$('#list').find('li').hide();
				 $('#list').find('li').each(function(){
				
				 	if ($(this).text().indexOf(inputVal)>=0) {
				 		$(this).show();
				 	}
				    
				  });
			});
		});
		$(document).on('click',function(e){
			if(e.target.id == 'race'){
				oUl.show();
			}else{
				oUl.hide();
			}		
		})


		var oInput1 = $('#race1');
		var oUl1 = $('#list1');
		var oLi1 = oUl.find('li');
		oInput1.on('focus',function(){
			oUl1.show();
			oUl1.delegate('li','click',function(){
				oInput1.val($(this).text());
				oUl1.hide();	
			});	
			 	
			$(this).on('input',function(){
			
				var inputVal1 = $(this).val();
				$('#list1').find('li').hide();
				 $('#list1').find('li').each(function(){
				
				 	if ($(this).text().indexOf(inputVal1)>=0) {
				 		$(this).show();
				 	}
				    
				  });
			});
		});
		$(document).on('click',function(e){
			if(e.target.id == 'race1'){
				oUl1.show();
			}else{
				oUl1.hide();
			}		
		})

		var oInput2 = $('#race2');
		var oUl2 = $('#list2');
		var oLi2 = oUl.find('li');
		oInput2.on('focus',function(){
			oUl2.show();
			oUl2.delegate('li','click',function(){
				oInput2.val($(this).html());
				oU2.hide();	
			});	
			 	
			$(this).on('input',function(){
			
				var inputVal2 = $(this).val();
				$('#list2').find('li').hide();
				 $('#list2').find('li').each(function(){
				
				 	if ($(this).text().indexOf(inputVal2)>=0) {
				 		$(this).show();
				 	}
				    
				  });
			});
		});
		$(document).on('click',function(e){
			if(e.target.id == 'race2'){
				oUl2.show();
			}else{
				oUl2.hide();
			}		
		})
	});
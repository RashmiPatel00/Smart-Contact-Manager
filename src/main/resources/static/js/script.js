console.log("welcome")

const togglesidebar = () =>{
    if($(".sidebar").is(":visible")){
           //true

                       $(".sidebar").css("display","none");
               $(".content").css("margin-left","0%");


    }else{
       //false
       $(".sidebar").css("display","block");
$(".content").css("margin-left","30%");
    }
};
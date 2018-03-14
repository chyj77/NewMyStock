function showLhb(tabid) {
    // alert(tabid);
    var rootDiv  = $('<div>', {'class':'l-tab-content-item'});
    var rqText =  $('<input>', {'type':'text', 'id':'datepicker1' ,'name':'rq'});
    var lhbDiv  = $('<div>', {'class':'l-tab-content-item','style':'overflow-y:scroll'});
    var date = new Date();
    date.setDate(date.getDate()-1);
    rqText.datepicker({//添加日期选择功能
        numberOfMonths:1,//显示几个月
        showButtonPanel:true,//是否显示按钮面板
        dateFormat: 'yy-mm-dd',//日期格式
        clearText:"清除",//清除日期的按钮名称
        closeText:"关闭",//关闭选择框的按钮名称
        yearSuffix: '年', //年的后缀
        showMonthAfterYear:true,//是否把月放在年的后面
        defaultDate:date,//默认日期
//    minDate:'2011-03-05',//最小日期
//    maxDate:'2011-03-20',//最大日期
        //monthNames: ['一月','二月','三月','四月','五月','六月','七月','八月','九月','十月','十一月','十二月'],
        //dayNames: ['星期日','星期一','星期二','星期三','星期四','星期五','星期六'],
        //dayNamesShort: ['周日','周一','周二','周三','周四','周五','周六'],
        //dayNamesMin: ['日','一','二','三','四','五','六'],
        onSelect: function(datepicker) {//选择日期后执行的操作
            console.log('datepicker:'+datepicker)
            lhbDiv.empty();
            executeShow(datepicker,tabid,lhbDiv)
        }
    });
    rqText.datepicker('setDate', date);
    rootDiv.append(rqText);
    rootDiv.append(lhbDiv);
    $("div[tabid='"+tabid+"']").append(rootDiv);
    var day = rqText.val();
    executeShow(day,tabid,lhbDiv);
}
function executeShow(day,tabid,lhbDiv) {
    $.ajax({
        type: 'GET',
        contentType: 'application/json',
        url: '/thsLhb',
        data:{'day':day},
        success: function (data) {
            // console.log(data);
            var jsonstr = JSON.parse(data);

            for(var i in jsonstr){
                console.log(jsonstr[i].rq);
                var rq = jsonstr[i].rq;
                var p = $('<p>', {'style':'font-size:18px;font-weight:bold'});
                p.text("日期： "+rq);
                lhbDiv.append(p);
                var data = jsonstr[i].data;
                for(var j in data){
                    var contentDiv  = $('<div>');
                    var stockcont = data[j].stockcont;
                    var stockcode = data[j].stockcode;
                    var stockname = data[j].stockname;
                    var nowprice = data[j].nowprice;
                    var zdf = data[j].zdf;
                    var cjje = data[j].cjje;
                    var detail = data[j].detail;
                    var cjDetail = data[j].cjDetail;
                    var mryyb = data[j].mryyb;
                    var mcyyb = data[j].mcyyb;
                    var pStockcont = $('<p>', {'style':'padding-top:8px;font-size:16px;font-weight:bold;color:red;'});
                    pStockcont.text(detail);
                    contentDiv.append(pStockcont);
                    var pDetail = $('<p>', {'style':'padding-top:8px;font-size:16px;font-weight:bold;'});
                    pDetail.text(" 现价："+nowprice+ " 涨跌幅："+zdf+"  "+ cjDetail);
                    contentDiv.append(pDetail);
                    var pMryyb = $('<p>', {'style':'padding-top:8px;font-size:14px;font-weight:bold'});
                    pMryyb.text("买入营业部：")
                    var mryybDiv  = $('<div>', {'id': data[j].stockcode+"_mryyb",'style':'float:left;'});
                    var mryybGrid  = $('<div>', {'id': data[j].stockcode+"_mryybGrid"});
                    var mryybData = {"Rows":mryyb,"Total":5};
                    mryybGrid.ligerGrid({
                        columns: [
                            { display: '营业部', name: 'yyb', align: 'left', width: 200, minWidth: 60 },
                            { display: '买入额', name: 'mre', minWidth: 100 },
                            { display: '卖出额', name: 'mce', minWidth: 100 },
                            { display: '净买额', name: 'jinge',minWidth: 100 }
                        ], data:mryybData, width:760, height:180,pageSize:5 ,rownumbers:true
                    });
                    mryybDiv.append(pMryyb);
                    mryybDiv.append(mryybGrid);
                    contentDiv.append(mryybDiv);
                    var pMcyyb = $('<p>', {'style':'padding-top:8px;font-size:14px;font-weight:bold;'});
                    pMcyyb.text("卖出营业部：")
                    var mcyybDiv  = $('<div>', {'id': data[j].stockcode+"_mcyyb",'style':'float:right;'});
                    var mcyybGrid  = $('<div>', {'id': data[j].stockcode+"_mcyybGrid"});
                    var mcyybData = {"Rows":mcyyb,"Total":5};
                    mcyybGrid.ligerGrid({
                        columns: [
                            { display: '营业部', name: 'yyb', align: 'left', width: 200, minWidth: 60 },
                            { display: '买入额', name: 'mre', minWidth: 100 },
                            { display: '卖出额', name: 'mce', minWidth: 100 },
                            { display: '净买额', name: 'jinge',minWidth: 100 }
                        ], data:mcyybData, width:760, height:180,pageSize:5 ,rownumbers:true
                    });
                    mcyybDiv.append(pMcyyb);
                    mcyybDiv.append(mcyybGrid);
                    contentDiv.append(mcyybDiv);
                    var bothDiv  = $('<div>', {'style':'clear:both;'});
                    contentDiv.append(bothDiv);
                    lhbDiv.append(contentDiv);

                }
            }
        }
    });
}
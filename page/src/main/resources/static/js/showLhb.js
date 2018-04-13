function showLhb(tabid) {
    // console.log(tabid);
    var rootDiv  = $('<div>', {'id':'lhb'+tabid,'class':'l-tab-content-item'});
    var p = $('<p>', {'style':'font-size:18px;font-weight:bold;float:left;display:inline;'});
    p.text("日期： ");
    var rqText =  $('<input>', {'type':'text', 'id':'txt1'});
    var lhbDiv  = $('<div>', {'class':'l-tab-content-item','style':'overflow-y:scroll'});
    rootDiv.append(rqText);
    rqText.parent().style='float:left';
    rqText.parent().prepend(p);
    var date = new Date();
    date.setDate(date.getDate()-1);
    // console.log(date);
    rqText.ligerDateEditor({initValue: date.format('yyyy-MM-dd') });
    rqText.blur(function ()
    {
        console.log('日期:',rqText.val());
        lhbDiv.empty();
        executeShow(rqText.val(),tabid,lhbDiv);
    });
    // console.log(rqText);
    // console.log(rqText.val());
    rootDiv.append(lhbDiv);
    $("div[tabid='"+tabid+"']").append(rootDiv);
    var day = rqText.val();
    executeShow(day,tabid,lhbDiv);
}
function executeShow(day,tabid,lhbDiv) {
    $.ajax({
        type: 'GET',
        contentType: 'application/json',
        url: '/lhb/index',
        data:{'day':day},
        success: function (data) {
            // console.log(data);
            var jsonstr = JSON.parse(data);

            for(var i in jsonstr){
                console.log(jsonstr[i].rq);
                var rq = jsonstr[i].rq;
                // var p = $('<p>', {'style':'font-size:18px;font-weight:bold'});
                // p.text("日期： "+rq);
                // lhbDiv.append(p);
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

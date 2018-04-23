var dxjlTable;
var zdtDiv ;

    function getDxjl(tabid) {
    var rootDiv = $('<div>', {'id': 'dxjlStock' + tabid, 'class': 'l-tab-content-item'});
    var lhbDiv = $('<div>', {'class': 'l-tab-content-item'});
    var clearDiv = $('<div>', {'class': 'l-clear'});
    zdtDiv = $('<div>', {'style': 'padding-top:10px'});
    var maingridDiv = $('<div>', {'id': 'maingridDxjlStock','class': 'l-tab-content-item','style':'overflow-y:scroll;padding-top:10px;'});
    var emptyBtn = "<span style='padding-left:30px;text-decoration:underline;' onclick='emptyZdt();'>清空</span>";
    dxjlTable=$('<table>', {'id': 'tableDxjlStock','border':'1px','cellspacing':'0','style':'padding-left:30px;padding-top:5px;width:100%'});
    var zdtspan = "<li style='padding-top:10px;'><span style='padding-left:30px;color:red'>涨停家数：<span style='text-decoration:underline;color:red'>0</span></span>"
        +"<span style='padding-left:30px;color:darkgreen'>跌停家数：<span style='text-decoration:underline;color:darkgreen'>0</span></span>"
        +"<span style='padding-left:30px;color:red'>上涨股票：<span style='text-decoration:underline;color:red'>0</span></span>"
        +"<span style='padding-left:30px;color:darkgreen'>下跌股票：<span style='text-decoration:underline;color:darkgreen'>0</span></span></li>";
    var zspan = "<li style='padding-top:10px;'><span style='padding-left:30px;color:red'>涨8%-10%：<span style='text-decoration:underline;color:red'>0</span></span>"
        +"<span style='padding-left:30px;color:red'>涨6%-8%：<span style='text-decoration:underline;color:red'>0</span></span>"
        +"<span style='padding-left:30px;color:red'>涨4%-6%：<span style='text-decoration:underline;color:red'>0</span></span>"
        +"<span style='padding-left:30px;color:red'>涨2%-4%：<span style='text-decoration:underline;color:red'>0</span></span>"
        +"<span style='padding-left:30px;color:red'>涨0%-2%：<span style='text-decoration:underline;color:red'>0</span></span></li>";
    var dspan = "<li style='padding-top:10px;'><span style='padding-left:30px;color:darkgreen'>跌8%-10%：<span style='text-decoration:underline;color:darkgreen'>0</span></span>"
        +"<span style='padding-left:30px;color:darkgreen'>跌6%-8%：<span style='text-decoration:underline;color:darkgreen'>0</span></span>"
        +"<span style='padding-left:30px;color:darkgreen'>跌4%-6%：<span style='text-decoration:underline;color:darkgreen'>0</span></span>"
        +"<span style='padding-left:30px;color:darkgreen'>跌2%-4%：<span style='text-decoration:underline;color:darkgreen'>0</span></span>"
        +"<span style='padding-left:30px;color:darkgreen'>跌0%-2%：<span style='text-decoration:underline;color:darkgreen'>0</span></span></li>";
    maingridDiv.append(emptyBtn);
    maingridDiv.append(dxjlTable);
    lhbDiv.prepend(clearDiv);
    zdtDiv.append(zdtspan);
    zdtDiv.append(zspan);
    zdtDiv.append(dspan);
    lhbDiv.append(zdtDiv);
    lhbDiv.append(maingridDiv);
    rootDiv.append(lhbDiv);
    $("div[tabid='"+tabid+"']").append(rootDiv);
}
function appendDxjl(dxjlData) {
    if(dxjlTable != undefined && dxjlTable !=null) {
        // console.log(dxjlTable);
        dxjlTable.prepend(dxjlData);
    }
}
function appendZdtData(zdtData) {
    zdtDiv.empty();
    var zdtJson = zdtData;
    var ztzs = zdtJson["ztzs"];
    // console.log("ztzs = ",ztzs);
    var dtzs = zdtJson["dtzs"];
    var z8to10 = zdtJson["z8to10"];
    var z6to8 = zdtJson["z6to8"];
    var z4to6 = zdtJson["z4to6"];
    var z2to4 = zdtJson["z2to4"];
    var z0to2 = zdtJson["z0to2"];
    var d0to2 = zdtJson["d0to2"];
    var d2to4 = zdtJson["d2to4"];
    var d4to6 = zdtJson["d4to6"];
    var d6to8 = zdtJson["d6to8"];
    var d8to10 = zdtJson["d8to10"];
    var dnum = zdtJson["dnum"];
    var znum = zdtJson["znum"];
    var zdtspan = "<li style='padding-top:10px;'><span style='padding-left:30px;color:red'>涨停家数：<span style='text-decoration:underline;color:red''>"+ztzs+"</span></span>"
        +"<span style='padding-left:30px;color:darkgreen'>跌停家数：<span style='text-decoration:underline;color:darkgreen''>"+dtzs+"</span></span>"
        +"<span style='padding-left:30px;color:red'>上涨股票：<span style='text-decoration:underline;color:red''>"+znum+"</span></span>"
        +"<span style='padding-left:30px;color:darkgreen'>下跌股票：<span style='text-decoration:underline;color:darkgreen''>"+dnum+"</span></span></li>";
    var zspan = "<li style='padding-top:10px;'><span style='padding-left:30px;color:red'>涨8%-10%：<span style='text-decoration:underline;color:red''>"+z8to10+"</span></span>"
        +"<span style='padding-left:30px;color:red'>涨6%-8%：<span style='text-decoration:underline;color:red''>"+z6to8+"</span></span>"
        +"<span style='padding-left:30px;color:red'>涨4%-6%：<span style='text-decoration:underline;color:red''>"+z4to6+"</span></span>"
        +"<span style='padding-left:30px;color:red'>涨2%-4%：<span style='text-decoration:underline;color:red''>"+z2to4+"</span></span>"
        +"<span style='padding-left:30px;color:red'>涨0%-2%：<span style='text-decoration:underline;color:red''>"+z0to2+"</span></span></li>";
    var dspan = "<li style='padding-top:10px;'><span style='padding-left:30px;color:darkgreen'>跌8%-10%：<span style='text-decoration:underline;color:darkgreen''>"+d8to10+"</span></span>"
        +"<span style='padding-left:30px;color:darkgreen'>跌6%-8%：<span style='text-decoration:underline;color:darkgreen''>"+d6to8+"</span></span>"
        +"<span style='padding-left:30px;color:darkgreen'>跌4%-6%：<span style='text-decoration:underline;color:darkgreen''>"+d4to6+"</span></span>"
        +"<span style='padding-left:30px;color:darkgreen'>跌2%-4%：<span style='text-decoration:underline;color:darkgreen''>"+d2to4+"</span></span>"
        +"<span style='padding-left:30px;color:darkgreen'>跌0%-2%：<span style='text-decoration:underline;color:darkgreen''>"+d0to2+"</span></span></li>";
    zdtDiv.append(zdtspan);
    zdtDiv.append(zspan);
    zdtDiv.append(dspan);
}
function emptyZdt() {
    dxjlTable.empty();
}
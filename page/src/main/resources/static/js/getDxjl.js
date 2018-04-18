var dxjlTable;
function getDxjl(tabid) {
    var rootDiv = $('<div>', {'id': 'dxjlStock' + tabid, 'class': 'l-tab-content-item'});
    var lhbDiv = $('<div>', {'class': 'l-tab-content-item','style':'overflow-y:scroll'});
    var clearDiv = $('<div>', {'class': 'l-clear'});
    var maingridDiv = $('<div>', {'id': 'maingridDxjlStock','class': 'l-tab-content-item','style':'overflow-y:scroll'});
    dxjlTable=$('<table>', {'id': 'tableDxjlStock','width':'100%','border':'1px','cellspacing':'0'});
    maingridDiv.append(dxjlTable);
    lhbDiv.prepend(clearDiv);
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
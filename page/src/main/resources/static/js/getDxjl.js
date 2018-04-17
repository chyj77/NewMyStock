var dxjlTable;
function getDxjl(tabid) {
    var rootDiv = $('<div>', {'id': 'dxjlStock' + tabid, 'class': 'l-tab-content-item'});
    var lhbDiv = $('<div>', {'class': 'l-tab-content-item'});
    var clearDiv = $('<div>', {'class': 'l-clear'});
    var maingridDiv = $('<div>', {'id': 'maingridDxjlStock'});
    dxjlTable=$('<table>', {'id': 'tableDxjlStock','style':'border-spacing:2px;border-color:grey'});
    maingridDiv.append(dxjlTable);
    lhbDiv.prepend(clearDiv);
    lhbDiv.append(maingridDiv);
    rootDiv.append(lhbDiv);
    $("div[tabid='"+tabid+"']").append(rootDiv);
}
function appendDxjl(dxjlData) {
    if(dxjlTable != undefined && dxjlTable !=null) {
        // console.log(dxjlTable);
        dxjlTable.append(dxjlData);
    }
}
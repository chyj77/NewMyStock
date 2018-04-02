function stock(tabid) {
    var rootDiv = $('<div>', {'id': 'marketStock' + tabid, 'class': 'l-tab-content-item'});
    var lhbDiv = $('<div>', {'class': 'l-tab-content-item'});
    var clearDiv = $('<div>', {'class': 'l-clear'});
    var maingridDiv = $('<div>', {'id': 'maingridMarketStock'});
    lhbDiv.prepend(clearDiv);
    lhbDiv.append(maingridDiv);
    rootDiv.append(lhbDiv);
    $("div[tabid='"+tabid+"']").append(rootDiv);
    $("#maingridMarketStock").ligerGrid({
        height: '95%',
        columns: [
            {
                display: '证券代码',
                name: 'stockcode', minWidth: 90, editor: {type: 'text'}
            },
            {
                display: '证券名称',
                name: 'stockname', minWidth: 90, editor: {type: 'text'}
            },
            {
                display: '上市时间',
                name: 'marketTime', minWidth: 90, editor: {type: 'text'}
            },
            {
                display: '总股数',
                name: 'totalNum', minWidth: 90, editor: {type: 'text'}
            },
            {
                display: '流通股数',
                name: 'floatNum', minWidth: 90, editor: {type: 'text'}
            },
            {
                display: '行业',
                name: 'hangye', minWidth: 90, editor: {type: 'text'}
            },
            {
                display: '标签',
                name: 'tag', minWidth: 90, editor: {type: 'text'}
            }
        ], data: stockData, pageSize: 25, rownumbers: true
    });
}
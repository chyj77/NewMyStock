function ztsjfx(tabid) {
    var rootDiv = $('<div>', {'id': 'ztsjFx' + tabid, 'class': 'l-tab-content-item'});
    var lhbDiv = $('<div>', {'class': 'l-tab-content-item'});
    var clearDiv = $('<div>', {'class': 'l-clear'});
    var maingridDiv = $('<div>', {'id': 'maingridGfjgdZtsjFx'});
    lhbDiv.prepend(clearDiv);
    lhbDiv.append(maingridDiv);
    rootDiv.append(lhbDiv);
    $("div[tabid='"+tabid+"']").append(rootDiv);
    $("#maingridGfjgdZtsjFx").ligerGrid({
        columns: [
            { display: '涨停最多概念', name: 'ztzdgn', align: 'left', width: 120,
                totalSummary:
                    {
                        type: 'count'
                    }
            },
            {
                display: '日期',
                name: 'rq', align: 'left', width: 90, minWidth: 60,type: 'text'
            },
            {
                display: '每日涨停数量',
                name: 'mrztgs', minWidth: 90, type: 'text',
                totalSummary:
                    {
                        type: 'sum,max'
                    }

            },
            {
                display: '非一字板个数',
                name: 'fyzbgs', minWidth: 90,type: 'text',
                totalSummary:
                    {
                        type: 'sum,max'
                    }

            },
            {
                display: '10点前涨停个数',
                name: 'dqztgs', minWidth: 90, type: 'text',
                totalSummary:
                    {
                        type: 'sum,max'
                    }

            },{
                display: '涨停最多个数',
                name: 'ztzdgs', minWidth: 90, type: 'text',
                totalSummary:
                    {
                        type: 'sum,max'
                    }

            },
            {
                display: '打板次日高开率',
                name: 'dbcrgkl', minWidth: 90, type: 'text'
            },
            {
                display: '收盘成功率',
                name: 'spcgl', minWidth: 90, type: 'text'
            },
            {
                display: '10点前涨停高开率',
                name: 'dqztgkl', minWidth: 90, type: 'text'
            },
            {
                display: '被砸数量',
                name: 'bzsl', minWidth: 90, type: 'text',
                totalSummary:
                    {
                        type: 'sum,max'
                    }

            },
            {
                display: '被砸率',
                name: 'bzl', type: 'text'
            }
        ], pageSize: 20,
        url: "/ztsj/fx", pageSizeOptions: [3, 10, 20, 30, 40, 50, 100],
        height: '100%', groupColumnName: 'ztzdgn', groupColumnDisplay: '涨停最多概念',alternatingRow:true
        // ,detail: { onShowDetail: function (r, p)
        //     {
        //
        //         // $(p).append($('<div>CustomerID：' + r.CustomerID + '</div>').css('margin', 20));
        //
        //     }
        // }
    });


}
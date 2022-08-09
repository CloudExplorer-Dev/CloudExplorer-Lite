import type { TableColumnCtx } from 'element-plus/es/components/table/src/table-column/defaults'
interface columnTypes {
    id: string,
    name: string,
    province: string,
    area: string,
    county: string,
    amount: Number
}
// 表格列配置
export const columns: any = [
    // 自定义索引
    {
        label: '排名',
        prop: 'ranking',
        type: 'index',
        width: 80,
        index: (index: number) => {
            return index * 3
        }
    },
    {
        prop: 'name',
        label: '名字',
        filters: [
            { text: '李白2', value: '李白2' },
            { text: '李白4', value: '李白4' }
        ],
        'filter-method': (value: string,
            row: columnTypes,
            column: TableColumnCtx<columnTypes>) => {
            const property = column['property']
            // @ts-ignore
            return row[property] === value
        },
        slotName: 'name'
    },
    {
        prop: 'address',
        label: '地址',
        children: [
            {
                label: '省份',
                prop: 'province',
                align: 'center'
            },
            {
                label: '城市',
                prop: 'city',
                align: 'center',
                children: [
                    {
                        label: '区',
                        prop: 'area',
                        align: 'center',
                    },
                    {
                        label: '县',
                        prop: 'county',
                        align: 'center',
                    }
                ]
            }
        ]
    },
    {
        prop: 'amount',
        label: '金额',
        sortable: true
    },
]
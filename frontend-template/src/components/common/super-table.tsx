import {Button, Card, Checkbox, CheckboxOptionType, Modal, Table, TableProps} from 'antd';
import {createStyles} from 'antd-style';
import React, {useEffect, useState} from 'react';

interface SuperTableProps<RecordType> extends TableProps<RecordType> {
  columns: TableProps<RecordType>['columns'];
  headTitle?: string;
  defaultVisibleColumns?: string[];
  tableKey: string; // 用于在localStorage中存储用户的列配置
  isColumnsSelectOpen: boolean; // 从外部传入模态框的显示状态
  setIsColumnsOpen: (visible: boolean) => void; // 从外部控制模态框显示的方法
}

const useStyle = createStyles(({css, token}) => {
  const {antCls} = token;
  return {
    customTable: css`
      ${antCls}-table {
        ${antCls}-table-container {
          ${antCls}-table-body,
          ${antCls}-table-content {
            scrollbar-width: thin;
            scrollbar-color: unset;
          }
        }
      }
    `,
  };
});

const SuperTable: React.FC<SuperTableProps<any>> = ({
                                                      columns,
                                                      headTitle,
                                                      defaultVisibleColumns,
                                                      tableKey,
                                                      isColumnsSelectOpen,
                                                      setIsColumnsOpen,
                                                      ...tableProps
                                                    }) => {
  const {styles} = useStyle();

  // 初始化时从localStorage读取或使用默认列
  const loadVisibleColumns = () => {
    let selectedTableColumns = localStorage.getItem("selectedTableColumns")
    if (selectedTableColumns) {
      selectedTableColumns = JSON.parse(selectedTableColumns)
    }
    const savedColumns = selectedTableColumns ? selectedTableColumns[tableKey] : null;
    if (savedColumns) {
      const visibleColumns = columns!.filter((col) => savedColumns.includes(col.key));
    //  console.debug('loadVisibleColumns', savedColumns, visibleColumns, visibleColumns!.map((col) => col.key));
      return visibleColumns;
    }
    const visibleColumns = defaultVisibleColumns
      ? columns!.filter((col) => defaultVisibleColumns.includes(col.key as string))
      : columns;
    //console.debug('loadVisibleColumns', columns, visibleColumns);
    return visibleColumns;

  };

  const [visibleColumns, setVisibleColumns] = useState(loadVisibleColumns());
  const [checkedValues, setCheckedValues] = useState([])

  useEffect(() => {
    // 当用户刷新页面时，重新从localStorage加载配置
    setVisibleColumns(loadVisibleColumns());
  }, [columns, tableKey, defaultVisibleColumns]);

  const handleOk = () => {
    // 保存当前的列配置到localStorage
    const newVisibleColumns = [...columns!.filter((col) => checkedValues.includes(col.key))];
   // console.debug("handleSelectedColumnChange->", newVisibleColumns)
    setVisibleColumns(newVisibleColumns);

    const currentVisibleKeys = newVisibleColumns!.map((col) => col.key);
    let selectedTableColumns = localStorage.getItem("selectedTableColumns")
    if (selectedTableColumns) {
      selectedTableColumns = JSON.parse(selectedTableColumns)
    } else {
      selectedTableColumns = {}
    }
    selectedTableColumns[tableKey] = currentVisibleKeys
    localStorage.setItem("selectedTableColumns", JSON.stringify(selectedTableColumns));
    setIsColumnsOpen(false);
  };

  const handleCancel = () => {
    setIsColumnsOpen(false);
  };

  const handleReset = () => {
    // 重置到默认配置并清除localStorage
    if (defaultVisibleColumns) {
      const resetColumns = columns!.filter((col) =>
        defaultVisibleColumns.includes(col.key as string),
      );
      setVisibleColumns(resetColumns);
      let selectedTableColumns = localStorage.getItem("selectedTableColumns")
      if (!selectedTableColumns) {
        return
      }
      selectedTableColumns = JSON.parse(selectedTableColumns)
      delete selectedTableColumns[tableKey]
      localStorage.setItem("selectedTableColumns", JSON.stringify(selectedTableColumns));
    }
  };

  const handleSelectedColumnChange = (checkedValues: any[]) => {
    setCheckedValues(checkedValues)
    const newVisibleColumns = [...columns!.filter((col) => checkedValues.includes(col.key))];
    // console.debug("handleSelectedColumnChange->", newVisibleColumns)
    setVisibleColumns(newVisibleColumns);
  };

  const checkboxOptions = columns!.map((col) => ({
    label: col.title,
    value: col.key,
  }));
  // visibleColumns!.map((col) => col.key) as CheckboxValueType[]
  return (
    <Card title={headTitle} bordered={true}>
      <Table
        size={'small'}
        // scroll={{
        //   x: '100vw',
        // }}
        scroll={{x: 'max-content'}}
        className={styles.customTable}
        columns={visibleColumns}
        {...tableProps}
      />
      <Modal
        title="选择要显示的列"
        open={isColumnsSelectOpen}
        onOk={handleOk}
        onCancel={handleCancel}
        footer={[
          <Button key="back" onClick={handleCancel}>
            取消
          </Button>,
          <Button key="reset" onClick={handleReset} type="primary" danger>
            重置
          </Button>,
          <Button key="submit" type="primary" onClick={handleOk}>
            保存
          </Button>,
        ]}
      >
        <Checkbox.Group
          options={checkboxOptions as CheckboxOptionType[]}
          defaultValue={visibleColumns!.map((col) => col.key)}
          // value={visibleColumns} // 使用 value 以反映当前状态
          onChange={handleSelectedColumnChange}
        />
      </Modal>
    </Card>
  );
};

export default SuperTable;

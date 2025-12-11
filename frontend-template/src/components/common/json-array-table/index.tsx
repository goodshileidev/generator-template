import JsonEditor, { JsonEditorProps } from '@/components/common/json-editor';
import { deepClone } from '@/utils';
import { Button, Form, Input, InputNumber, Popconfirm, Segmented, Table, Typography } from 'antd';
import React, { useEffect, useMemo, useState } from 'react';

interface JsonArrayTableProps {
  defaultMode?: 'json' | 'table';
  value?: any[];
  onChange?: (value: any) => void;
  readonly?: boolean;
}

//  转换json数组到表格列配置
const convertJsonToColumns = <T extends Record<string, any>>(data: T[]): any[] => {
  if (data.length === 0) return [];

  const sample = data[0];

  const buildColumns = (currentObj: any, parentPath: string = ''): any[] => {
    return Object.keys(currentObj).map((key) => {
      const value = currentObj[key];
      const currentPath = parentPath ? `${parentPath}.${key}` : key;
      const isArray = Array.isArray(value);
      const isObject = typeof value === 'object' && value !== null && !isArray;

      const column: any = {
        title: key,
        dataIndex: currentPath,
        editable: true,
        type: isArray || isObject ? 'group' : 'item',
      };

      if (isArray) {
        // 处理数组类型
        const arrayItem = value[0];
        if (arrayItem && typeof arrayItem === 'object' && !Array.isArray(arrayItem)) {
          column.children = buildColumns(arrayItem, currentPath);
        } else {
          column.children = [];
        }
      } else if (isObject) {
        // 处理对象类型
        column.children = buildColumns(value, currentPath);
      }
      return column;
    });
  };

  if (typeof sample !== 'object') {
    return [
      {
        title: 'Param',
        dataIndex: 'param',
        editable: true,
      },
    ];
  }

  return buildColumns(sample);
};

interface EditableCellProps extends React.HTMLAttributes<HTMLElement> {
  editing: boolean;
  dataIndex: string;
  title: any;
  inputType: 'number' | 'text';
  record: any;
  index: number;
}

interface ModeSwitchProps {
  mode: 'json' | 'table';
  setMode: (mode: ModeSwitchProps['mode']) => void;
}

const ModeSwitch: React.FC<ModeSwitchProps> = ({ mode, setMode }) => {
  return (
    <Segmented<'json' | 'table'>
      value={mode}
      options={[
        { label: '表格', icon: '', value: 'table' },
        { label: 'Json', icon: '', value: 'json' },
      ]}
      onChange={(value) => setMode(value)}
    />
  );
};

//  编辑单元格
const EditableCell: React.FC<React.PropsWithChildren<EditableCellProps>> = ({
  editing,
  dataIndex,
  title,
  inputType,
  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  record,
  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  index,
  children,
  ...restProps
}) => {
  const inputNode = inputType === 'number' ? <InputNumber /> : <Input />;
  return (
    <td {...restProps}>
      {editing ? (
        <Form.Item
          name={dataIndex}
          style={{ margin: 0 }}
          rules={[
            {
              required: true,
              message: `Please Input ${title}!`,
            },
          ]}
        >
          {inputNode}
        </Form.Item>
      ) : (
        children
      )}
    </td>
  );
};

//  Json表格
const TableView: React.FC<
  JsonArrayTableProps & {
    mode: ModeSwitchProps['mode'];
    setMode: ModeSwitchProps['setMode'];
  }
> = ({ value, readonly, mode, setMode, onChange }) => {
  const [columns, setColumns] = useState<any[]>([]);

  const [form] = Form.useForm();
  const [editingKey, setEditingKey] = useState(-1);
  const [isSample, setIsSample] = useState(false);

  const [backValue, setBackValue] = useState<any[]>([]);

  const handleChange = (changes: any[]) => {
    if (isSample) {
      onChange?.(changes.map((i) => i.param));
    } else {
      onChange?.(changes);
    }
  };

  //  是否编辑中
  const isEditing = (index: number) => index === editingKey;
  //  编辑
  const edit = (record: Partial<any> & { key: React.Key }, index: number) => {
    form.setFieldsValue({ ...record });
    setEditingKey(index);
  };

  const remove = (index: number) => {
    handleChange(backValue.filter((_, bIndex) => bIndex !== index));
  };

  //  取消编辑
  const cancel = (record: any) => {
    if (record.isNew) {
      setBackValue(backValue.filter(i => !i.isNew))
    }
    setEditingKey(-1);
  };

  const save = async (index: number) => {
    const item = await form.validateFields();
    if (item.isNew) {
      delete item.isNew;
    }
    handleChange(backValue.map((bItem, bIndex) => (bIndex === index ? item : bItem)));
    setEditingKey(-1);
  };

  const handleAdd = () => {
    const newItem = {
      isNew: true,
    };
    columns.forEach((col) => {
      //  目前只处理一级路径
      if (col.dataIndex) {
        newItem[col.dataIndex] = '';
      }
    });
    setBackValue([newItem, ...backValue].map((_, i) => ({ ..._, $index: i })));
    form.setFieldsValue({ ...newItem });
    setEditingKey(0);
  };
  const editColumn = useMemo(
    () => ({
      title: '操作',
      fixed: 'right',
      width: 150,
      render: (_: any, record: any, index: number) => {
        const editable = isEditing(index);
        return editable ? (
          <span>
            <Typography.Link onClick={() => save(index)} style={{ marginInlineEnd: 8 }}>
              保存
            </Typography.Link>
            <Popconfirm title="确认取消编辑吗?" onConfirm={() => cancel(record)}>
              <a>取消</a>
            </Popconfirm>
          </span>
        ) : (
          <span>
            <Typography.Link
              disabled={editingKey !== -1}
              style={{ marginInlineEnd: 8 }}
              onClick={() => edit(record, index)}
            >
              编辑
            </Typography.Link>
            <Popconfirm
              disabled={editingKey !== -1}
              title="确认删除该条数据吗?"
              onConfirm={() => remove(index)}
            >
              <Button type="link" danger>
                删除
              </Button>
            </Popconfirm>
          </span>
        );
      },
    }),
    [editingKey, backValue],
  );
  useEffect(() => {
    if (value) {
      const cols = convertJsonToColumns(value).map((col) => {
        if (!col.editable) {
          return col;
        }
        return {
          ...col,
          onCell: (record: any, index: number) => ({
            record,
            inputType: 'text',
            dataIndex: col.dataIndex,
            title: col.title,
            editing: isEditing(index),
          }),
        };
      });
      if (readonly !== true) {
        cols.push(editColumn);
      }
      setColumns(cols);
    }
  }, [value, editingKey]);

  useEffect(() => {
    if (value && value.length) {
      const _isSimple = typeof value[0] !== 'object';
      setIsSample(_isSimple);
      if (_isSimple) {
        setBackValue(
          value.map((item, index) => ({
            param: item,
            $index: index,
          })),
        );
      } else {
        setBackValue(deepClone(value).map((_, i) => ({ ..._, $index: i })));
      }
    }
  }, [value]);

  return (
    <>
      <div className={'table-wrapper'}>
        <div className={'table-toolbar--wrapper'} style={{ textAlign: 'right', marginBottom: 8 }}>
          {readonly !== true && (
            <Button
              type={'primary'}
              disabled={editingKey !== -1}
              style={{ marginRight: 8 }}
              onClick={handleAdd}
            >
              新增
            </Button>
          )}
          <ModeSwitch mode={mode} setMode={setMode} />
        </div>
        <Form form={form} component={false}>
          <Table
            scroll={{ x: '100%' }}
            components={{
              body: { cell: EditableCell },
            }}
            rowKey="$index"
            columns={columns}
            dataSource={backValue}
            bordered
          />
        </Form>
      </div>
    </>
  );
};

const JsonView: React.FC<
  JsonEditorProps & {
    mode: ModeSwitchProps['mode'];
    setMode: ModeSwitchProps['setMode'];
  }
> = ({ mode, setMode, ...jsonProps }) => {
  return (
    <div className="json-wrapper">
      <div className={'toolbar-wrapper'} style={{ textAlign: 'right', marginBottom: 8 }}>
        <ModeSwitch mode={mode} setMode={setMode} />
      </div>
      <JsonEditor {...jsonProps} />
    </div>
  );
};

//  Json数组表格
const JsonArrayTable: React.FC<JsonArrayTableProps> = ({
  defaultMode,
  value,
  onChange,
  readonly,
}) => {
  const [mode, setMode] = useState(defaultMode || 'table');

  return (
    <>
      <div className={'json-array-table'} style={{ width: '100%' }}>
        {mode === 'json' ? (
          <JsonView
            value={value}
            onChange={onChange}
            readonly={readonly}
            mode={mode}
            setMode={setMode}
          />
        ) : (
          <TableView
            value={value}
            onChange={onChange}
            readonly={readonly}
            mode={mode}
            setMode={setMode}
          />
        )}
      </div>
    </>
  );
};

export default JsonArrayTable;

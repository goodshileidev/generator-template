import {MinusCircleOutlined, PlusOutlined} from '@ant-design/icons';
import {Button, Form, Input, InputNumber, Space} from 'antd';
import {useTranslation} from 'react-i18next';
import DynamicSelect, {RelationMappingType} from './dynamic-select/dynamic-select-index';

//  ComponentType
type FieldComponentType = 'input' | 'select' | 'textarea' | 'text' | 'number' | string;

//  下拉选项配置
interface SelectDataOption {
  label: string;
  value: string;

  [key: string]: any;
}

//  动态表单List属性
interface DynamicFormListField {
  //  显示标题
  title: string;
  //  目前没用到
  key: string;
  //  与dataIndex同理，用于匹配下拉
  fieldKey?: string;
  //  数据属性名称
  dataIndex: string;
  //  组件类型
  componentType: FieldComponentType;
  //  下拉选项
  selectOptions?: SelectDataOption[];
  //  过滤下拉选项时是隐藏还是禁用
  filterOpt?: 'disabled' | 'hidden';
  getOptionsCallback?: any,
  relationMapping?: RelationMappingType;
  fieldMapping?: any;
  width?: any;
  //  自定义渲染(目前很多地方写了错误的render，暂不识别)
  render?: (index: number) => JSX.Element;
  showLabel: true
  listHeight?: number
}

//  动态表单List输入参数
interface DynamicFormListProps {
  title: string;
  showLabel: string
  fieldName: string;
  fields: DynamicFormListField[];
  onAddData: any
  defaultValue: any
}

const DynamicFormList: React.FC<DynamicFormListProps> = (props) => {
  const {t} = useTranslation();
  const formInstance = Form.useFormInstance();
  const [dynamicForm] = Form.useForm();


  const renderComponentByType = (index: number, field: DynamicFormListField) => {
    //  // 现在很多render写的都有问题，暂不支持
    // if (field.render && typeof field.render === 'function') {
    //   return field.render();
    // }
    switch (field.componentType) {
      case 'input':
        return <Input allowClear placeholder={field.title} style={{width: field.width ? field.width : 150}}></Input>;
      case 'text':
        return <Input allowClear placeholder={field.title} style={{width: field.width ? field.width : 150}}></Input>;
      case 'hidden':
        return <Input allowClear placeholder={field.title} style={{"display": "none"}}></Input>;
      case 'textarea':
        return <Input.TextArea allowClear placeholder={field.title} style={{width: field.width ? field.width : 150}}/>;
      case 'number':
        return <InputNumber placeholder={field.title} style={{width: field.width ? field.width : 150}}/>;
      case 'link':
        return field.render && typeof field.render === 'function' ? field.render(index) : <></>;
      case 'select':
        return (
          <DynamicSelect
            showSearch
            allowClear
            listHeight={field.listHeight ? field.listHeight : 250}
            popupMatchSelectWidth={false}
            virtual={false}
            optionFilterProp="label"
            placeholder={field.title}
            filterOption={field.filterOpt}
            style={{width: field.width ? field.width : 200}}
            options={field.selectOptions}
            getOptionsCallback={field.getOptionsCallback}
            fieldName={field.dataIndex || field.fieldKey}
            fieldPath={[props.fieldName]}
            filterOpt={field.filterOpt}
            relationMapping={field.relationMapping}
            fieldMapping={field.fieldMapping}
            listIndex={index}
          />
        );
      default:
        return <span>{field.componentType}暂未支持</span>;
    }
  };
  const renderFormList = () => {
    return <Form.List name={props.fieldName}>
      {(fields, {add, remove}) => (
        <>
          {fields.map(({key, name, ...restField}, index) => (
            <Space key={key} style={{display: 'flex', marginBottom: 8}} align="baseline">
              <MinusCircleOutlined onClick={() => remove(name)}/>
              {props.fields.map((field) => {
                if (field.componentType !== "hidden") {
                  let label = field.title
                  if (!props.showLabel) {
                    label = ""
                  }
                  return (
                    <>
                      <Form.Item
                        {...restField}
                        label={label}
                        name={[name, field.dataIndex]}
                      >
                        {renderComponentByType(name, field)}
                      </Form.Item>
                    </>
                  );
                } else {
                  return <></>
                }
              })}
            </Space>

          ))}
          <Form.Item>
            <Button type="dashed" onClick={() => {
              add(props.defaultValue)
            }}
                    block icon={<PlusOutlined/>}>
              {t('common.button.add')}
            </Button>
          </Form.Item>
        </>
      )}
    </Form.List>
  }
  return (
    <>
      {formInstance ? renderFormList() :
        <Form
          form={dynamicForm}
          layout="vertical"
        >
          {renderFormList()}
        </Form>}
    </>
  );
};

export default DynamicFormList;

import React from 'react';
import ReactJson, { ReactJsonViewProps } from 'react-json-view';

export interface JsonEditorProps extends Omit<ReactJsonViewProps, 'src'>  {
  value?: any;
  src?: any;
  readonly?: boolean;
  onChange?: (value: any) => void;
}

const JsonEditor: React.FC<JsonEditorProps> = ({
  value,
  readonly,
  onChange,
  ...props
}) => {
  const handleEdit: ReactJsonViewProps['onEdit'] = (edit) => {
    const { updated_src } = edit;
    onChange?.(updated_src);
  };
  const handleDelete: ReactJsonViewProps['onDelete'] = (del) => {
    const { updated_src } = del;
    onChange?.(updated_src);
  }

  const handleAdd: ReactJsonViewProps['onAdd'] = (add) => {
    const { updated_src } = add;
    onChange?.(updated_src);
  }
  return (
    <ReactJson
      name={false}
      displayDataTypes={false}
      displayObjectSize={false}
      theme="monokai"
      onEdit={readonly ? false : handleEdit}
      onAdd={readonly ? false : handleAdd}
      onDelete={readonly ? false : handleDelete}
      {...props}
      src={value}
    />
  );
};

export default JsonEditor;

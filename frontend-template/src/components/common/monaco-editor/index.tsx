import { DiffEditor, DiffEditorProps, Editor, type EditorProps } from '@monaco-editor/react';
import React, { useEffect, useState } from 'react';

const MonacoEditor: React.FC<EditorProps> = (props) => {
  return <Editor {...props} />;
};

const MonacoDiffEditor: React.FC<DiffEditorProps> = (props) => {
  return <DiffEditor {...props} />;
};

interface JsonEditorProps extends EditorProps {
  value?: any;
  transform?: boolean;
  onChange?: (value: any, e: any) => void;
}

const JsonEditor: React.FC<JsonEditorProps> = ({ value, transform, onChange, ...props }) => {
  const [bindValue, setBindValue] = useState('');
  useEffect(() => {
    try {
      if (value) {
        const v = typeof value === 'string' ? value : JSON.stringify(value, null, 2);
        setBindValue(v);
      }
    } catch {
      console.error('转换数据结构失败');
    }
  }, [value]);

  const handleChange: EditorProps['onChange'] = (v, e) => {
    try {
      setBindValue(v || '');
      if (transform !== false) {
        const parseValue = v ? JSON.parse(v) : v;
        onChange?.(parseValue, e);
      } else {
        onChange?.(v, e);
      }
    } catch {
      console.error('转换数据结构失败');
      onChange?.(v, e);
    }
  };
  return (
    <Editor
      height={200}
      theme="vs-dark"
      {...props}
      value={bindValue}
      language="json"
      onChange={handleChange}
    />
  );
};

export { MonacoEditor, MonacoDiffEditor, JsonEditor };

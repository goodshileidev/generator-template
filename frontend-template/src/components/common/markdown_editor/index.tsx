import { MonacoEditor, MonacoDiffEditor } from '@/components/common/monaco-editor';
import { Segmented } from 'antd';
import { useEffect, useState } from 'react';
import Markdown from 'react-markdown';
import './index.less';

type EditorModeType = 'edit' | 'preview' | 'diff' | 'editpreview';

interface MarkdownEditorProps {
  defaultMode?: EditorModeType;
  value?: string;
  height?: string | number;
  showToolbar?: boolean;
  originValue?: string;
  onChange?: (value?: string) => void;
}

const MarkdownEditor: React.FC<MarkdownEditorProps> = ({
  showToolbar,
  defaultMode,
  value,
  originValue,
  height,
  onChange,
}) => {
  const handleValueChange = (value?: string) => {
    if (onChange && typeof onChange === 'function') {
      onChange(value);
    }
  };

  const [beforeValue, setBeforeValue] = useState('');
  useEffect(() => {
    setBeforeValue(originValue || value || '');
  }, [originValue]);

  const [mode, setMode] = useState<EditorModeType>(defaultMode || 'preview');

  const getClassNames = () => {
    const names = ["markdown-editor", `markdown-editor__${mode}`];
    if (showToolbar !== false) {
      names.push('has-toolbar');
    }
    return names.join(' ')
  }

  const renderContent = () => {
    switch (mode) {
      case 'preview':
        return <Markdown>{value}</Markdown>;
      case 'edit':
        return <MonacoEditor value={value} language="markdown" onChange={handleValueChange} />;
      case 'editpreview':
        return [
          <div className="preview-aside--wrapper" key="preview">
            <Markdown>{value}</Markdown>
          </div>,
          <div className="edit-aside--wrapper" key="edit">
            <MonacoEditor value={value} language="markdown" onChange={handleValueChange} />
          </div>,
        ];

      case 'diff':
        return (
          <MonacoDiffEditor
            language="markdown"
            original={beforeValue}
            modified={value}
            onMount={(editor) => {
              const modifyEditor = editor.getModifiedEditor();
              if (modifyEditor) {
                modifyEditor.onDidChangeModelContent(() => {
                  handleValueChange(modifyEditor.getValue());
                });
              }
            }}
          />
        );
    }
  };
  return (
    <div className={getClassNames()} style={{ height: height || 400 }}>
      {showToolbar !== false && (
        <div style={{ textAlign: 'right', paddingBottom: 8 }}>
          <Segmented<EditorModeType>
            value={mode}
            options={[
              {
                label: '预览',
                value: 'preview',
              },
              {
                label: '编辑',
                value: 'edit',
              },
              {
                label: '实时编辑',
                value: 'editpreview',
              },
              {
                label: '对比',
                value: 'diff',
              },
            ]}
            onChange={(value) => setMode(value)}
          ></Segmented>
        </div>
      )}
      <div
        className="markdown-editor--inner"
      >
        {renderContent()}
      </div>
    </div>
  );
};

export default MarkdownEditor;

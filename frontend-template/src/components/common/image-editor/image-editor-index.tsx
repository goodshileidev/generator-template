import ImgEditor from '@/components/var_field/ImgEditor';
import {UploadOutlined} from '@ant-design/icons';
import type {GetProp, ModalProps, UploadFile, UploadProps} from 'antd';
import {Upload} from 'antd';
import axios from 'axios';
import React, {useEffect, useState} from 'react';
import './index.less';

type FileType = Parameters<GetProp<UploadProps, 'beforeUpload'>>[0];

//  获取Base64
const getBase64 = (data: FileType | Blob): Promise<string> => {
  return new Promise((resolve, reject) => {
    const reader = new FileReader();
    reader.readAsDataURL(data);
    reader.onload = () => resolve(reader.result as string);
    reader.onerror = (error) => reject(error);
  });
};

//  根据Url获取Base64
const getBase64ByUrl = async (url: string): Promise<string> => {
  const file = await axios.get(url, {responseType: 'blob'});
  return await getBase64(file.data as Blob);
};

interface ImageEditorProps {
  alwaysShowArea?: boolean;
  onBase64Change?: (fileName: string, url: string) => void;
  onFileChange?: (fileName: string, filePath: string) => void;
  initFile?: {
    fileName: string;
    filePath: string;
    bizCategory: string;
  } | null;
}

const ImageEditor: React.FC<ImageEditorProps> = ({onBase64Change, initFile, onFileChange, alwaysShowArea}) => {
  const [previewImg, setPreviewImg] = useState('');

  const [previewImgName, setPreviewImgName] = useState('');

  const [showEditModal, setShowEditModal] = useState(false);

  const [editBase64Url, setEditBase64Url] = useState('');

  const [fileList, setFileList] = useState<UploadFile[]>([]);

  useEffect(() => {
    console.debug("ImageEditor.initFile", initFile)
    if (!initFile) {
      return;
    }
    const {fileName, filePath} = initFile;
    setFileList([
      {
        uid: fileName,
        name: fileName,
        status: 'done',
        url: `${filePath}`,
      },
    ]);
  }, [initFile]);

  useEffect(() => {
    if (!editBase64Url) {
      return;
    }
    if (onBase64Change && typeof onBase64Change === 'function') {
      onBase64Change(previewImgName, editBase64Url);
    }
  }, [editBase64Url]);

  const handleChange: UploadProps['onChange'] = (info) => {
    if (info.fileList.length) {
      const file = info.fileList[0];
      if (file.status === 'done') {
        if (file.response && file.response.data) {
          const fileName = file.name;
          const filePath = file.response.data[0].downloadUrl;
          if (onFileChange && typeof onFileChange === 'function') {
            onFileChange(fileName, filePath);
          }
        }
      }
    }
    setFileList(info.fileList);
  };

  const onEditorImgBase64Change = (url: string) => {
    setEditBase64Url(url);
  };

  const handleOk: ModalProps['onOk'] = () => {
    setShowEditModal(false);
  };

  const handleModalCancel: ModalProps['onCancel'] = () => {
    setShowEditModal(false);
  };

  const handlePreview: UploadProps['onPreview'] = (file) => {
    const promise = file.originFileObj
      ? getBase64(file.originFileObj as FileType)
      : file.url
        ? getBase64ByUrl(file.url)
        : null;
    if (promise) {
      promise.then((url) => {
        setPreviewImg(url);
        setPreviewImgName(file.name);
        setShowEditModal(true);
      });
    }
  };

  const uploadButton = (
    <button style={{border: 0, background: 'none'}} type="button">
      <UploadOutlined/>
      <div style={{marginTop: 8}}>Upload</div>
    </button>
  );
  return (
    <>
      <div className="image-editor">
        <Upload
          accept="image/*"
          name="files"
          listType="picture-card"
          maxCount={1}
          multiple={false}
          fileList={fileList}
          action={"/api/common/file-manager/create-files/" + initFile.category}
          onChange={handleChange}
          onPreview={handlePreview}
        >
          {fileList.length >= 1 ? null : uploadButton}
        </Upload>
        <ImgEditor
          alwaysShowArea={alwaysShowArea}
          modalImgUrl={previewImg}
          isModalOpen={showEditModal}
          handleOk={handleOk}
          handleCancel={handleModalCancel}
          onImgBase64ValueChange={onEditorImgBase64Change}
        ></ImgEditor>
      </div>
    </>
  )
    ;
};

export default ImageEditor;

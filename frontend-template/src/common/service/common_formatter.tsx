import React from "react";
import {Image} from "antd"
import dayjs from "dayjs";
import {LockOutlined} from '@ant-design/icons'

export function renderDateInRow(text: string, row: any, index: number) {
  if (!text) {
    return <></>
  }
  const result = new Date(text)
  return dayjs(result).format("YYYY-MM-DD");
}

export function renderTimeInRow(text: string, row: any, index: number) {
  if (!text) {
    return <></>
  }
  const result = new Date(text)
  return dayjs(result).format("HH:mm:ss");
}

export function renderTime2InRow(text: string, row: any, index: number) {
  if (!text) {
    return <></>
  }
  const result = new Date(text)
  return dayjs(result).format("HH:mm");
}

export function renderDateTimeInRow(text: string, row: any, index: number) {
  if (!text) {
    return <></>
  }
  const result = new Date(text)
  return dayjs(result).format("YYYY-MM-DD HH:mm:ss");
}

export function renderDateTime2InRow(text: string, row: any, index: number) {
  if (!text) {
    return <></>
  }
  const result = new Date(text)
  return dayjs(result).format("YYYY-MM-DD HH:mm");
}


export function renderYearMonthInRow(text: string, row: any, index: number) {
  if (!text) {
    return <></>
  }
  const result = new Date(text)
  return dayjs(result).format("YYYY-MM");
}

export function renderDateTime(text: string) {
  if (!text) {
    return <></>
  }
  const result = new Date(text)
  return dayjs(result).format("YYYY-MM-DD HH:mm:ss");
}

export function renderLocked(text: string, row: any) {
  if (!text) {
    return <></>
  }
  return row.isLocked && row.isLocked === "1" ? <>
    {text}&nbsp;&nbsp;<span color="yellow"><LockOutlined/>
  </span></> : text;
}


export function renderImageInRow(text: string) {
  if (!text) {
    return <></>
  }
  // console.debug("renderTimeInRow", text, result)
  if (text.indexOf("http") > -1) {
    return (<>
      <Image src={text}
             height={100}
             style={{
               maxHeight: 200
             }}/></>);
  } else if (text.indexOf("/api/") > -1) {
    return (<><Image height={100} style={{
      maxHeight: 200
    }} src={text}/></>);
  } else {
    return (<><Image height={100} style={{
      maxHeight: 200
    }} src={"/api/common/file-manager/get/" + text}/></>);
  }
}

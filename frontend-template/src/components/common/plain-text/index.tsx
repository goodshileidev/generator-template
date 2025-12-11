import { ReactNode } from "react";

export interface PlainTextProps {
  value?: ReactNode;
}
const PlainText: React.FC<PlainTextProps> = ({
  value
}) => {
  return <span>{value}</span>
}

export default PlainText;

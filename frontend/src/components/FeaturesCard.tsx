import { ReactElement } from "react";

interface Props {
  title: string;
  text: string;
  children?: ReactElement | ReactElement[];
  image: ReactElement;
}
export const FeatureCard = ({ text, title, children, image }: Props) => {
  return (
    <div className="p-4 flex flex-col items-center gap-4 border-2 border-body rounded-lg flex-1 bg-black">
      <div className="flex flex-col items-center gap-2">
        {image}
        <h3 className="h3">{title}</h3>
      </div>
      <p className="p text-center">{text}</p>
      {children}
    </div>
  );
};

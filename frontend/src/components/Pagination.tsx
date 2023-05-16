import { PaginationButton } from "./PaginationButton";

interface props {
  numberOfWebsites: number;
  currentActive: number;
}
export const Pagination = ({ numberOfWebsites, currentActive }: props) => {
  return (
    <div className="flex items-center gap-2" >
      {Array.from(
        Array(Math.floor(numberOfWebsites / 10) + (numberOfWebsites % 10 ? 1 : 0))
      ).map((x, i) => (
        <PaginationButton active={currentActive === i + 1} number={i + 1} />
      ))}
    </div>
  );
};

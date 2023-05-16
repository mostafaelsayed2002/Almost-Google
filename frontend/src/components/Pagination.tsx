import { PaginationButton } from "./PaginationButton";

interface PaginationProps {
  numberOfWebsites: number;
  currentActive: number;
}

export const Pagination = ({ numberOfWebsites, currentActive }: PaginationProps) => {
  const totalPages = Math.floor(numberOfWebsites / 10) + (numberOfWebsites % 10 ? 1 : 0);
  const displayRange = 10; // Number of pagination buttons to display at a time
  let startPage = Math.max(currentActive - Math.floor(displayRange / 2), 1);
  let endPage = Math.min(startPage + displayRange - 1, totalPages);

  if (totalPages <= displayRange) {
    startPage = 1;
    endPage = totalPages;
  } else if (currentActive <= Math.floor(displayRange / 2)) {
    endPage = displayRange;
  } else if (currentActive >= totalPages - Math.floor(displayRange / 2)) {
    startPage = totalPages - displayRange + 1;
    endPage = totalPages;
  }

  return (
    <div className="flex items-center gap-2">
      {currentActive > 1 && (
        <PaginationButton number={currentActive - 1} active={false} element="<" />
      )}
      {Array.from(Array(endPage - startPage + 1)).map((_, i) => (
        <PaginationButton key={startPage + i} active={currentActive === startPage + i} number={startPage + i} />
      ))}
      {currentActive < totalPages && (
        <PaginationButton number={currentActive + 1} active={false} element=">" />
      )}
    </div>
  );
};

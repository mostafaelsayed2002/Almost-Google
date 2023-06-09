import Link from "next/link";
import { useRouter } from "next/router";

interface props {
  number: number;
  active: boolean;
  element?: string; 
}
export const PaginationButton = ({ number, active, element }: props) => {
  const router = useRouter();
  const { Input } = router.query;

  return (
    <a
      key={number}
      className={`border-2 ${
        active ? "border-white text-white" : "border-body text-body"
      } px-4 py-2 rounded-lg font-roboto`}
      href={`/result?Input=${Input}&page=${number}`}
    >
      {element? element: number}
    </a>
  );
};
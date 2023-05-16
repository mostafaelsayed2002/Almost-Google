import { Pagination } from "@/components/Pagination";
import { PaginationButton } from "@/components/PaginationButton";
import { Result } from "@/components/Result";
import { result } from "@/utils/types/result";
import { useRouter } from "next/router";
import { ReactElement, useEffect, useState } from "react";

export default function ResultPage() {
  const router = useRouter();
  const { Input, page } = router.query;
  const [data, setData] = useState<result[]>();
  
  const [isLoading, setIsLoading] = useState(true);
  const [noResult, setNoResults] = useState(false);

  const [numberOfWebsites, setNumberOfWebsites] = useState(0);

  useEffect (() => {
    setData([]);
    setIsLoading(true);
    setNoResults(false);
  }, [router.query.searchterm, router.query.pageNumber])

  useEffect(() => {
    if (!Input) return;
    setIsLoading(true);
    const fetchResults = async () => {
      setIsLoading(true);
      const res = await fetch(`http://localhost:8080/?Input=${Input}&page=${page as string}`);
      const data = await res.json();
      setIsLoading(false);
      setData(data.websites);
      setNumberOfWebsites(data.numberOfWebsites)
      if (data.length == 0)
        setNoResults(true)
    };
    fetchResults();
  }, [Input]);
  return (
    <div className="flex flex-col gap-4 py-8 items-center w-full">
      {data && data.length
        ? data.map((result, i) => (
            <Result
              isLoading={isLoading}
              brief={result.brief}
              title={result.title}
              url={result.url}
              stem={result.stem}
              key={i}
              word={Input ? Input?.toString() : ""}
            />
          ))
        : !noResult ? Array.from(Array(10).keys()).map((i) => (
            <Result isLoading={true} brief="" title="" url="" word="" stem="" key={i} />
          )) : null}
      {
        noResult ?
        <section className="flex flex-col gap-4">
          <p className="text-6xl font-roboto text-white text-center">404</p>
          <p className="p">Search for something else</p>
        </section>
        : null
      }
       {
        <Pagination currentActive={Number.parseInt(page as string)} numberOfWebsites={numberOfWebsites} />
       }
    </div>
  );
}

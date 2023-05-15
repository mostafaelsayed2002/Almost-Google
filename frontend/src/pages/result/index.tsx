import { Result } from "@/components/Result";
import { result } from "@/utils/types/result";
import { useRouter } from "next/router";
import { useEffect, useState } from "react";

export default function ResultPage() {
  const router = useRouter();
  const { searchterm } = router.query;
  const [data, setData] = useState<result[]>();
  const [isLoading, setIsLoading] = useState(true);
  const [noResult, setNoResults] = useState(false);

  useEffect (() => {
    setData([]); 
    setIsLoading(true);
    setNoResults(false);
  }, [router.query.searchterm])
  
  useEffect(() => {
    if (!searchterm) return;
    setIsLoading(true);
    const fetchResults = async () => {
      setIsLoading(true);
      const res = await fetch(`http://localhost:8080/?Input=${searchterm}`);
      const data = await res.json();
      setIsLoading(false);
      setData(data);
      console.log(data);
      if (data.length == 0)
        setNoResults(true)
    };
    fetchResults();
  }, [searchterm]);
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
              word={searchterm ? searchterm?.toString() : ""}
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
    </div>
  );
}

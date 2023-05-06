import { Result } from "@/components/Result";
import { result } from "@/utils/types/result";
import { useRouter } from "next/router";
import { useEffect, useState } from "react";

export default function ResultPage() {
  const router = useRouter();
  const { searchterm } = router.query;
  const [data, setData] = useState<result[]>();
  const [isLoading, setLoading] = useState(false);

  useEffect(() => {
    if (!searchterm) return;
    setLoading(true);
    const fetchResults = async () => {
      setLoading(true);
      const res = await fetch(`http://localhost:8080/?Input=${searchterm}`);
      const data = await res.json();
      setLoading(false);
      setData(data);
    };
    fetchResults();
  }, [searchterm]);
  return (
    <div className="flex flex-col gap-4 py-8 items-center w-full">
      {data
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
        : Array.from(Array(10).keys()).map((i) => (
            <Result isLoading={true} brief="" title="" url="" word="" stem="" key={i} />
          ))}
    </div>
  );
}

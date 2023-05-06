import { Logo } from "@/components/Logo";
import { Result } from "@/components/Result";
import { SearchBar } from "@/components/SearchBar";
import axios from "axios";
import { useRouter } from "next/router";
import { useEffect, useState } from "react";
interface result {
  brief: string;
  title: string;
  url: string;
}
axios.defaults.headers.get['Access-Control-Allow-Origin'] = '*';
export default function ResultPage() {
  const router = useRouter();
  const { searchterm } = router.query;
  const [data, setData] = useState<result[]>();
  const [isLoading, setLoading] = useState(false);

  useEffect(() => {
    if (!searchterm)
      return; 
    setLoading(true);
    const fetchResults = async () => {
      setLoading(true);
      const res = await fetch(`http://localhost:8080/?Input=${searchterm}`,
      {
        mode: "no-cors"
      });
      const data = await res.json();
      console.log(data);
    };
    fetchResults();
  }, [searchterm]);
  return (
    <div className="flex flex-col gap-16 items-center py-16">
      {data ? data.map((reuslt) => <Result />) : null}
    </div>
  );
}

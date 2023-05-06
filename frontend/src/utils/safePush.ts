import { Router, useRouter } from "next/router";
import { useEffect, useState } from "react";

export const useSafePush = () => {
  const [calledPush, setCalledPush] = useState(false);
  const router = useRouter();
  const safePush = (path: string) => {
      router.push(path);
  };
  useEffect(() => {
    if (calledPush) {
      return; // no need to call router.push() again
    }
    setCalledPush(true); // <-- toggle 'true' after first redirect
  }, [router]);
  return { safePush };
};

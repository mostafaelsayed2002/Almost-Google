import { Logo } from "@/components/Logo";
import { Result } from "@/components/Result";
import { SearchBar } from "@/components/SearchBar";

export default function ResultPage() {
  return (
    <div className="flex flex-col gap-16 items-center py-16">
      <div className="flex flex-col gap-8 w-full items-center">
        <Logo />
        <SearchBar />
      </div>
      <div className="flex flex-col w-full gap-4">
        <section className="flex flex-col">
          <Result />
          <Result />
          <Result />
          <Result />
          <Result />
        </section>
        <div className="flex flex-row w-full justify-center gap-2">
          <button className="bg-black text-white rouned rounded-lg text-center py-2 px-4 border ">
            1
          </button>
          <button className="bg-black text-body  rouned rounded-lg text-center py-2 px-4 border border-body ">
            2
          </button>
          <button className="bg-black text-body  rouned rounded-lg text-center py-2 px-4 border border-body ">
            3
          </button>
        </div>
      </div>

      <h3 className="h3 place-self-center">About Us</h3>
    </div>
  );
}

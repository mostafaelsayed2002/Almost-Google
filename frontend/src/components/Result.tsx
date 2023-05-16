import { result } from "@/utils/types/result";
import { ReactElement } from "react";
import { stemmer } from "stemmer";

interface props extends result {
  isLoading: boolean;
  word: string;
}
const HighlightedResult = ({
  brief,
  word,
  stem,
}: {
  brief: string;
  word: string;
  stem: string;
}) => {
  const briefWords = brief.replaceAll(/\.\B/gm, " ").replaceAll(/\[\d+\]/gm, " ").split(" ");
  const wordStem = stemmer(word.toLowerCase());
  let indexOfWord = 0; 
  for (let i = 0; i < briefWords.length; i++) {
    if (stemmer(briefWords[i]) === wordStem) {
      indexOfWord = i; 
      break; 
    }
  }
  const res = [];
  for (
    let i = (indexOfWord - 5 < 0 ? 0 : indexOfWord-5);
    i <
    (briefWords.length - indexOfWord > 30
      ? indexOfWord + 30
      : briefWords.length);
    i++
  )
    res.push(
      stemmer(briefWords[i].toLowerCase()) === wordStem ? (
        <span className="em">{briefWords[i] + " "}</span>
      ) : (
        briefWords[i] + " "
      )
    );

  return <p className="p">{res}</p>;
};
export const Result = ({ isLoading, brief, title, url, word, stem }: props) => {
  return isLoading ? (
    <section className="flex flex-col gap-2 py-2 w-full relative">
      <div className="w-1/5 h-4 bg-white bg-opacity-20 rounded-lg animate-pulse"></div>
      <div className="w-2/5 h-4 bg-link bg-opacity-20 rounded-lg animate-pulse" />
      <div className="w-full h-4 bg-white bg-opacity-20 rounded-lg animate-pulse" />
      <div className="w-3/5 h-4 bg-white bg-opacity-20 rounded-lg animate-pulse" />
      <div className=" bg-white mt-2 bg-opacity-50 h-[2px]"></div>
    </section>
  ) : (
    <section className="flex flex-col gap-2 py-2 w-full relative">
      <h3 className="h3 text-white">{title}</h3>
      <a href={url} target="_blank" rel="noopener noreferrer" className="link">
        {url}
      </a>
      <HighlightedResult brief={brief} word={word} stem={stem} />
      <div className=" bg-white bg-opacity-50 h-[2px]"></div>
    </section>
  );
};



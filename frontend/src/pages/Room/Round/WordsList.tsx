interface Props {
  words: string[];
}

export const WordsList = (props: Props) => {
  const { words } = props;

  return <>
    <div>Słowa:</div>
    <div>Kliknij aby ukryć (nie działa)</div>
    {words.map((word) => (
      <div key={word}>
        [ ] {word}
      </div>
    ))}
  </>;
};
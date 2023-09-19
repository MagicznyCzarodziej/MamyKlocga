interface Props {
    challenge: string;
}

export const Challenge = (props: Props) => {
    const { challenge } = props

    return <div>
      <div className={`mt-4 text-center text-xl text-gray-500`}>Wyzwanie:</div>
      <div className={`text-center text-2xl`}>{challenge}</div>
    </div>
}

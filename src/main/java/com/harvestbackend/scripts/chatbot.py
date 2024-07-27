import os
import argparse
import ast
from openai import Client

OPENAI_API_KEY = ''
os.environ['OPENAI_API_KEY'] = OPENAI_API_KEY
client = Client()
OPENAI_ASSISTANT_ID = ''
MODEL = 'gpt-3.5-turbo'
NAME = 'Harvest Chatbot'
INSTRUCTIONS = ('You are a chatbot for an agricultural product bidding website that helps users with navigating the '
                'website and solving any problems they may have with its features. You will explain to the users in '
                'Hindi or English in simple, concise terms such that anybody could understand your instructions.')


def init_assistant(model, name, instructions):
    assistant = client.beta.assistants.create(model=model,
                                              name=name,
                                              instructions=instructions)
    return assistant


def get_response(assistant_id, messages, name):
    thread = client.beta.threads.create(messages=messages)
    run = client.beta.threads.runs.create(thread_id=thread.id,
                                          assistant_id=assistant_id,
                                          temperature=0.7,
                                          additional_instructions='You will help the user concisely and in simple '
                                                                  'words. You will absolutely not reply to anything '
                                                                  f'not related to the website. You may refer to the '
                                                                  f'user as {name}')
    while run.status != 'completed':
        run = client.beta.threads.runs.retrieve(run_id=run.id, thread_id=thread.id)
        continue
    if run.status == 'completed':
        response = client.beta.threads.messages.list(thread_id=thread.id)
        return response.data[0].content[0].text.value
    else:
        return 'Error processing your request. Please try again.'


def main():
    parser = argparse.ArgumentParser(description='Chatbot')
    parser.add_argument('--messages', type=str)
    parser.add_argument('--name', type=str)
    args = parser.parse_args()
    user_fullname = args.name
    message_list = ast.literal_eval(args.messages)
    print(get_response(assistant_id=OPENAI_ASSISTANT_ID, messages=message_list, name=user_fullname))


if __name__ == '__main__':
    main()
